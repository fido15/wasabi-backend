

from langchain_ollama import ChatOllama
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser



class RAGModel:
 def __init__(self,Prompt,question,documents):
        self.Prompt = Prompt
        self.question=question
        self.documents=documents
 def reseachDoc (self,Docs):
    

    prompt = PromptTemplate(
        template=Prompt,
        input_variables=["question", "documents"]
    )
    formatted_prompt = prompt.format(question=question, documents=documents)
    
    llm = ChatOllama(
        model="llama3.1",
        temperature=0
    )

    rag_chain = prompt | llm | StrOutputParser
   
