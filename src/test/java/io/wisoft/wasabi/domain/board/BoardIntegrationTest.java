package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId(), member.getName(), member.getRole());

            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var perform = mockMvc.perform(post("/boards")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(json));

            // then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.id").exists());
        }

        @DisplayName("요청시 로그인 상태여야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board_fail1(final Member member) throws Exception {

            // given
            memberRepository.save(member);

            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var perform = mockMvc.perform(post("/boards")
                    .contentType(APPLICATION_JSON)
                    .content(json));

            // then
            perform.andExpect(status().isUnauthorized());
        }

        @DisplayName("요청시 제목과 본문은 필수다.")
        @ParameterizedTest
        @AutoSource
        void write_post_fail2(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId(), savedMember.getName(), savedMember.getRole());

            final WriteBoardRequest request = new WriteBoardRequest(
                    "    ",
                    null,
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var perform = mockMvc.perform(post("/boards")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(json));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            final Board board = boardRepository.save(
                    Board.createBoard(
                            "title",
                            "content",
                            member
                    ));

            //when
            final var result = mockMvc.perform(get("/boards/{boardId}", board.getId())
                    .contentType(APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.views").value(1));
        }


        @Test
        @DisplayName("존재하지 않는 게시글을 조회하려 할 경우, 조회에 실패한다.")
        void read_not_found_board() throws Exception {

            //given

            //when
            final var result = mockMvc.perform(get("/boards/{boardId}", 10000L)
                    .contentType(APPLICATION_JSON));

            //then
            result.andExpect(status().isNotFound());
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시물들이 반환된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void read_my_boards(
                final Member member
        ) throws Exception {

            // given
            memberRepository.save(member);

            final List<Board> boards = List.of(
                    Board.createBoard(
                            "title",
                            "content",
                            member
                    ),
                    Board.createBoard(
                            "title",
                            "content",
                            member
                    )
            );
            boardRepository.saveAll(boards);

            final List<Like> likes = List.of(
                    new Like(member, boards.get(0)),
                    new Like(member, boards.get(1))
            );
            likeRepository.saveAll(likes);

            final String accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole()
            );

            // when
            final var result = mockMvc.perform(
                    get("/boards/my-board")
                            .param("page", "0")
                            .param("size", "3")
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "bearer " + accessToken)
            );

            // then
            result.andExpect(status().isOk());
        }

        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요한 게시물들이 반환된다.")
        void read_my_like_boards(final Member member, final Board board1, final Board board2) throws Exception {

            // given
            new Like(member, board1);
            new Like(member, board2);

            final String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole());

            // when
            final var result = mockMvc.perform(get("/boards/my-like")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken));

            // then
            result.andExpect(status().isOk());
        }
    }
}