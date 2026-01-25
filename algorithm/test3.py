from service import AlarmService
import cv2
import subprocess
from qcloud_cos import CosConfig
from qcloud_cos import CosS3Client
from util import Logger
import queue
import threading
logger = Logger.setup_logger()


def upload2Cos(list,uuid):
    # 配置腾讯云COS服务
    secret_id = ''
    secret_key = ''
    region = 'ap-beijing'
    bucket = 'hospital-alarm-1318141347'
    cos_config = CosConfig(Region=region, SecretId=secret_id, SecretKey=secret_key)
    cos_client = CosS3Client(cos_config)
    cnt = 0

    # 创建一个缓存列表，用于存储每一帧的字节流
    frame_list = []

    while cnt != len(list):
        frame = list[cnt]
        _, jpeg_frame = cv2.imencode('.jpg', frame)
        image_bytes = jpeg_frame.tobytes()
        # 将编码后的帧数据添加到缓存列表
        frame_list.append(image_bytes)
        cnt += 1
    logger.info("get frame list")
    # 拼接缓存列表中的帧数据
    video_bytes = b''.join(frame_list)
    command = ['ffmpeg', '-i', 'pipe:0', '-c:v', 'libx264', '-f', 'flv', 'pipe:1']
    process = subprocess.Popen(command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    # 创建队列用于存储输出流
    output_queue = queue.Queue()

    # 定义函数用于读取子进程的输出流
    def read_output():
        while True:
            output = process.stdout.read(4096)
            if output:
                output_queue.put(output)
            else:
                break

    # 创建线程读取子进程的输出流
    output_thread = threading.Thread(target=read_output)
    output_thread.start()

    # 将输入流写入FFmpeg的标准输入流
    process.stdin.write(video_bytes)
    process.stdin.close()

    # 等待子进程完成
    process.wait()

    # 等待输出线程结束
    output_thread.join()
    # 从队列中读取输出流
    output_stream = b''
    while not output_queue.empty():
        output_stream += output_queue.get()
    # 上传字节流到腾讯云COS服务
    object_key = uuid + '.flv'  # 上传的对象键
    cos_client.put_object(Bucket=bucket, Key=object_key, Body=output_stream)
    print("upload success")


upload2Cos("hello")
