package io.wisoft.wasabi.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import io.wisoft.wasabi.domain.board.Board;

public class NotSaveBoardCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(Board.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    static Board factory(final ObjectGenerationContext context) {
        return Board.createBoard(
                "title",
                "content",
                NotSaveMemberCustomization.factory(context)
        );
    }
}