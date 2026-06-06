# Please install OpenAI SDK first: `pip3 install openai`
import os
from openai import OpenAI

client = OpenAI(
    api_key="sk-46f8a46dc5754643b6a1a49ec14590e6",
    base_url="https://api.deepseek.com")

response = client.chat.completions.create(
    model="deepseek-v4-flash",
    messages=[
        {"role": "system", "content": "You are a helpful assistant"},
        {"role": "user", "content": "你记得我之前和你的对话吗？"},
    ],
    stream=True,
    reasoning_effort="high",
    extra_body={"thinking": {"type": "enabled"}}
)

#流式输出
for chunk in response:
    if chunk.choices[0].delta.content:
       print(
          chunk.choices[0].delta.content, 
          end='',
          flush=True
          )
    
