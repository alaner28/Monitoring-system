<template>
  <div class="panel">
    <div id="demoDiv">
      <div class="videoDiv">
        <video ref="videoElement" class="video-js vjs-default-skin" controls autoplay></video>
      </div>

      <el-button @click="drawer = true" type="primary" class="btn">
        点击查看监控列表
      </el-button>

      <el-drawer
        title="监控列表"
        :visible.sync="drawer"
        :direction="direction"
        :before-close="handleClose">
        <div class="lists">
          <div
            class="list"
            v-for="(item, index) in monitorLists"
            :key="index"
            @click="checkmonitor(index)">
            <div class="t1">
              <div class="t11">{{ item.name }}</div>
              <div class="t12">
                <div class="text4" :style="item.running ? fontStyle[0] : fontStyle[1]">
                  {{ item.running ? '正在运行' : '已停用' }}
                </div>
                <div class="img4">
                  <img :src="item.running ? require('../../public/assets/running.png') : require('../../public/assets/unrunning.png')" alt="">
                </div>
              </div>
            </div>
            <div class="t2">
              <div class="t21">
                <h4>摄像头编号：{{ item.number }}</h4>
                <h4>监控区域：{{ item.dapartment }}</h4>
                <h4>区域负责人：{{ item.leader }}</h4>
              </div>
              <div class="t22">
                <el-button type="primary" icon="el-icon-edit" class="t22btn">编辑</el-button>
              </div>
            </div>
          </div>
        </div>
      </el-drawer>
    </div>
    <div class="panel-footer"></div>
  </div>
  
</template>

<script>
// import videojs from 'video.js'; // 引入 video.js
// import 'video.js/dist/video-js.css'; // 引入 video.js 样式
// import 'videojs-flash'; // 引入 RTMP 支持
import flvjs from 'flv.js';
// import Hls from 'hls.js';

export default {
  name: 'monitor-1',
  data() {
    return {
      flvPlayer: null, // 更改为 flvPlayer
      fontStyle: [
        { color: '#06BFA1' },
        { color: 'red' },
      ],
      drawer: false,
      direction: 'rtl',
      monitorLists: [
        {
          name: '1号摄像头',
          number: '001',
          dapartment: '2号楼',
          leader: '小警',
          running: true,
          video: 'http://your-flv-url1.flv', // 示例 FLV 地址
        },
        {
          name: '2号摄像头',
          number: '002',
          dapartment: '3号楼',
          leader: '小明',
          running: true,
          video: 'http://your-flv-url2.flv',
        },
        {
          name: '3号摄像头',
          number: '003',
          dapartment: '大厅',
          leader: '小红',
          running: true,
          video: 'http://your-flv-url3.flv',
        },
        {
          name: '4号摄像头',
          number: '004',
          dapartment: '28号楼',
          leader: '小白',
          running: false,
          video: null,
        },
      ],
    };
  },
  
  mounted() {

    this.getVideoData();
  },
      
  beforeDestroy() {
    if (this.flvPlayer) {
      this.flvPlayer.destroy(); // 销毁 flv.js 播放器实例
    }
  },
  
  methods: {
    initializeVideoPlayer(videoUrl) {
      console.log('videourl',videoUrl);
      
      if (flvjs.isSupported()) {
        const videoElement = this.$refs.videoElement;
        const flvPlayer = flvjs.createPlayer({
          type: 'flv',
          // url: 'assets/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
          // url:videoUrl
          //url: 'https://citydefender-1326073552.cos.ap-beijing.myqcloud.com/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
          url:'http://play2.city-guardian.top/live/test.flv?auth_key=2593435398-0-0-ef66e48403f3fa546987f347f72f0b7a'
        });
        flvPlayer.attachMediaElement(videoElement);
        flvPlayer.load();
        // 使用用户交互播放视频
        videoElement.addEventListener('click', () => {
          flvPlayer.play().catch(error => {
            console.log('Autoplay failed:', error);
          });

        });
      }
    },
    getVideoData() {

      // const video = 'http://play1.city-guardian.top/live/test.flv?auth_key=1728140343-0-0-4233a23962cf2a9e8eb3fd7d5b36ac2f'
      // this.initializeVideoPlayer(video);


      const token = window.sessionStorage.getItem('token');
      this.$axios({
        method: 'GET',
        url: 'http://8.152.219.117:10215/api/v1/monitor',
        headers: {
          Authorization: token,
        },
      })
      .then((response) => {
        if (response.data.code === 'D0400') {
          this.$message({
            message: 'token过期，请重新登录',
            type: 'warning',
          });
          this.$router.push('/login');
        } else {
          console.log('获取监控列表成功', response);
          const data = response.data.data;
          this.monitorLists = data; // 更新监控列表

          let id = window.sessionStorage.getItem('monitorId') || 0;
          id = parseInt(id);
          
          // 在这里调用初始化视频播放器，将数据传递进去
          if (data[id].video) {
          console.log('video',data[id].video);
          
            this.initializeVideoPlayer(data[id].video);
          }
        }
      })
      .catch((error) => {
        console.error('Error fetching video data:', error);
      });
    },


    handleClose(done) {
      // 在关闭抽屉之前，暂停视频
      if (this.flvPlayer) {
        this.flvPlayer.pause(); // 暂停视频
      }
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
          console.log(_);
          
        })
        .catch(_ => {
          console.log(_);
        });
    },

    checkmonitor(index) {
      const selectedVideoUrl = this.monitorLists[index].video;
      window.sessionStorage.setItem('monitorId', index);

      // 调用播放视频的函数
      this.playFlvVideo(selectedVideoUrl);
      // 关闭 el-drawer
      this.drawer = false;
    },

    playFlvVideo(videoUrl) {
      if (videoUrl) {
        this.initializeVideoPlayer(videoUrl); // 调用初始化播放器方法
      }
    },
  },
};

</script>

<style scoped>
#demoDiv {
  width: 32rem;
  height: 19rem;
  position: relative;
}

.list {
  width: 85%;
  height: 8rem;
  margin: 0 auto;
  border-radius: 1.2rem;
  background-color: #e8eefe;
  margin-bottom: 2rem;
  padding: 1rem 1.7rem;
  cursor: pointer;
  color: #636b95;
}

.list .t1 {
  display: flex;
  justify-content: space-between;
}

.list .t1 .t11 {
  font-size: 1.8rem;
  font-weight: 600;
}

.list .t1 .t12 {
  display: flex;
}

.list .t1 .t12 .text4 {
  margin-right: 0.8rem;
  font-size: 1.5rem;
}

.list .t1 .t12 .img4 img {
  width: 1.7rem;
  height: 1.7rem;
}

.list .t2 {
  margin-top: 0.6rem;
  display: flex;
}

.list .t2 .t21 {
  flex: 3;
}

.list .t2 .t21 h4 {
  font-size: 1.2rem;
  text-align: left;
}

.list .t2 .t22 {
  flex: 2;
}

.list .t2 .t22 .t22btn {
  margin-top: 1.2rem;
  margin-left: 2.5rem;
}

.btn {
  width: 12rem;
  position: absolute;
  top: -3%;
  right: 0%;
}

.videoDiv {
  width: 100%;
  height: 93%;
  margin-top: 0.8rem;
}

.panel {
  padding: 0 0;
}

video {
  width: 100%;
  height: 100%;
  margin: 0 auto;
  margin-top: -0.6rem;
  object-fit: cover; /* 使视频填充容器并裁剪超出部分 */
}
</style>