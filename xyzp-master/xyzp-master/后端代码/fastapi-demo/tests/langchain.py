from langchain_deepseek import ChatDeepSeek
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_community.embeddings import HuggingFaceEmbeddings

llm = ChatDeepSeek(
    model="deepseek-chat",  # 推荐使用 deepseek-v4-flash 或 deepseek-v4-pro
    api_key="sk-46f8a46dc5754643b6a1a49ec14590e6",
    base_url="https://api.deepseek.com",
    temperature=0.7,
)

# 使用 stream 方法进行流式生成
# response = llm.stream(input="请发挥你的创造力讲一个笑话")

#聊天模型流式生成示例
messages = [
    SystemMessage(content="你是一个幽默的笑话大王，你熟读古今笑话与幽默故事选集，擅长用幽默的语言讲述笑话。"),
    HumanMessage(content="请发挥你的创造力讲一个笑话")
]
response = llm.stream(input=messages)


for chunk in response:
    if chunk:
       print(chunk.content,end='', flush=True)  

#文本嵌入模型完成文本向量化
# embeddings = HuggingFaceEmbeddings(model_name="sentence-transformers/all-MiniLM-L6-v2")

# print(embeddings.embed_query("你好，世界！"))