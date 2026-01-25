import cv2
import ffmpeg

# 打开本地摄像头
cap = cv2.VideoCapture(0)

# 设置编码参数
output_size = (640, 480)
fps = 30
codec = "libx264"
bitrate = "1000k"

# 设置输出流
rtmp_url = "rtmp://192.168.115.51:1985/live/test"
out = ffmpeg.output(
    ffmpeg.input('pipe:', format='rawvideo', pix_fmt='bgr24', s='{}x{}'.format(*output_size), r=fps),
    rtmp_url,
    vcodec=codec,
    b=bitrate
)
print('output stream: {}'.format(out))
# 打开输出流
process = out.run_async(pipe_stdin=True)

# 循环读取每一帧图像，并将其写入输出流
while True:
    print('reading...')
    ret, frame = cap.read()
    if not ret:
        break
    process.stdin.write(frame.tobytes())

# 关闭输入流和输出流
cap.release()
process.stdin.close()
process.wait()
