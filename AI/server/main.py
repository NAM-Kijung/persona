import json
import io
import base64
import asyncio
from typing import Optional
from fastapi import FastAPI, WebSocket
import cv2
import numpy as np
from fer import FER

from fastapi import FastAPI, File, UploadFile

import openai
# import mysql.connector
# import boto3
from kiwipiepy import Kiwi
from starlette.middleware.cors import CORSMiddleware
from pydub import AudioSegment;

openai.api_key = "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"
# origins = ["*"]

origins = ["http://localhost:3000"]
kiwi = Kiwi()

app = FastAPI()
detector = FER()
# uvicorn main:app --reload
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/audio/")
async def get_audio_file(file: UploadFile = File(...)):
    print("실행")
    file_location = f"uploads/{file.filename}"
    with open(file_location, "wb") as file_object:
        file_object.write(file.file.read())
    audio_file = open(f"{file_location}", "rb")

    print(audio_file)
    transcript = openai.Audio.transcribe("whisper-1", audio_file)
    text = transcript['text']
    print("=======api로 실행한 text========")
    print(text)
    # transcript_ja = openai.Audio.translate("whisper-1", audio_content, target_language="ja", file_format="mp3")

    # text = transcript['text']
    # print("model로 돌리기")
    # modelT = model.transcribe(file_location)
    # print(modelT)
    # for r in modelT['segments']:
    #     print(f'[{r["start"]} --> {r["end"]}] {r["text"]}')

    # if content_type.startswith("audio/"):
    #     audio = AudioSegment.from_file(file_content)
    # audio = AudioSegment.from_file(file.file, format=file.content_type.split("/")[-1])
    # audio.export("output.mp3", format="mp3")

    # with open(file_location, "wb") as file_object:
    #     file_object.write(file.file.read())

    # audio = AudioSegment.from_file(file_location, format=file.content_type.split("/")[-1])
    # print("위에서 에러인가?")
    # audio_filename = os.path.splitext(file.filename)[0] + ".wav"
    # audio_file_path = f"audio/{audio_filename}"
    # audio.export(audio_file_path, format="mp3")
    # print("audio란")

    # print(type(file_location))
    # print("=============모델로 돌린 번역=============")
    # resultSegment = model.transcribe(file_location)
    # print(resultSegment)
    # print("===================영어번역===============")
    # resulten = model.transcribe(f"{file_location}", task='translate')
    # print(resulten)
    # print(resulten['text'])
    # print("=================일본어 번역===============")
    # resultja = model.transcribe(f"{file_location}", language='ja')
    # print(resultja)
    # print(resultja['text'])
    # print("=================스페인어 번역===============")
    # resultes = model.transcribe(f"{file_location}", language='es')
    # print(resultes['text'])
    # print("=====================세그먼트 별로 나누기===================")
    # for r in resultSegment['segments']:
    #     print(f'[{r["start"]} --> {r["end"]}] {r["text"]}')

    # print("=====================api 문장 별로 나누기======================")
    # sentence = kiwi.split_into_sents(text)
    # print(sentence)
    # for st in sentence:
    #     print(st.text)

    # return {"message": {text}, "sentence": {sentence}}
    return {"message": {text}}



@app.post("/script/save")
async def save_script(script: str):
    sentence = kiwi.split_into_sents(script)
    StList = []
    for st in sentence:
        StList.append(sentence[st])

    return {"scripts": {StList}}


@app.websocket("/")
async def websocket_endpoint(websocket: WebSocket):
    # await asyncio.sleep(0.1)
    await websocket.accept()
    # while True:
    try:
        payload = await websocket.receive_text()
        payload = json.loads(payload)
        imageByt64 = payload['data']['image'].split(',')[1]
        # decode and convert into image
        image = np.fromstring(base64.b64decode(imageByt64), np.uint8)
        image = cv2.imdecode(image, cv2.IMREAD_COLOR)
        prediction = detector.detect_emotions(image)
        response = {
            "predictions": prediction[0]['emotions'],
            "emotion": max(prediction[0]['emotions'], key=prediction[0]['emotions'].get)
        }
        await websocket.send_json(response)

        websocket.close()
    except:
        websocket.close()
