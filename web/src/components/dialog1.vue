<template>
    <el-dialog
      title="报警信息"
      class="dialog"
      :visible.sync="localDialogVisible"
      width="30%"
      :before-close="handleClose">
      <span>
        <div class="video">
          <video ref="videoElement" muted controls></video>
        </div>
        <el-table
        :data="tableData"
         stripe
        style="width: 100%">
        <el-table-column width="30">
        </el-table-column>
        <el-table-column
          prop="key">
        </el-table-column>
        <el-table-column
          prop="val"
          width="160">
        </el-table-column>
      </el-table>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="closeDialog">确 定</el-button>
      </span>
    </el-dialog>
  </template>
  
  <script>
import flvjs from 'flv.js';
  export default {
    name: 'dialog-1',
    props: ['item'],
    data() {
      return {
        localDialogVisible: true,
        tableData: [
            {
              key:'事件类型',
              val:this.item.eventName,
            }, {
              key:'区域',
              val:this.item.department,
            },{
              key:'时间',
              val:this.item.date,
            }, {
              key:'等级',
              val:this.item.level,
            },{
              key:'处理结果',
              val:this.item.deal,
            }
          ]
      };
    },
    mounted(){
      this.getVideoData();
    },
    watch: {
      // 监听 dialogVisible1 的变化
      dialogVisible1(newVal) {
        this.localDialogVisible = newVal;
      }
    },
    methods: {
      closeDialog() {
        console.log('关闭了');
        
        this.localDialogVisible = false; // 修改本地变量
        this.$emit('updateDialogVisible1', this.localDialogVisible); // 通知父组件更新
      },
      handleClose() {
        this.localDialogVisible = false; // 处理关闭操作
        this.$emit('updateDialogVisible1', this.localDialogVisible); // 同时通知父组件
      },

      getVideoData() {
        console.log('获取视频',this.item.video);

    //     if (flvjs.isSupported()) {
    //     const videoElement = this.$refs.videoElement;
    //     const flvPlayer = flvjs.createPlayer({
    //     type: 'flv',
    //     url: '/assets/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
    //     });
    //     flvPlayer.attachMediaElement(videoElement);
    //     flvPlayer.load();
    //     flvPlayer.play();
    // }
        // if (flvjs.isSupported()) {
        //                   const videoElement = this.$refs.videoElement;
        //                   const flvPlayer = flvjs.createPlayer({
        //                   type: 'flv',
        //                   url: 'assets/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
        //                   // url:this.item.video
        //                   // url: 'https://citydefender-1326073552.cos.ap-beijing.myqcloud.com/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
        //                   });
        //                   flvPlayer.attachMediaElement(videoElement);
        //                   flvPlayer.load();
        //                   // 使用用户交互播放视频
        //                   videoElement.addEventListener('click', () => {
        //                     flvPlayer.play().catch(error => {
        //                       console.log('Autoplay failed:', error);
        //                     });

        //                   });
        //               }
        const token = window.sessionStorage.getItem('token');
        this.$axios({
          method: 'GET',
          url: 'http://8.152.219.117:10215/api/v1/monitor',
          headers: {
            Authorization: token,
          },
        })
                    .then(response => {
                        console.log('diglog',response.data.chartData);
                        
                        this.video = response.data.chartData;  // 将响应数据绑定到组件状态中
                        // console.log(this.video.videoPath);

                        if (flvjs.isSupported()) {
                          const videoElement = this.$refs.videoElement;
                          const flvPlayer = flvjs.createPlayer({
                          type: 'flv',
                          // url: 'assets/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
                          url:this.item.video
                          // url: 'https://citydefender-1326073552.cos.ap-beijing.myqcloud.com/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv'
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
                        // var player = TCPlayer("player-container-id", {});
                        // player.src('https://citydefender-1326073552.cos.ap-beijing.myqcloud.com/1ad2d92b-fdf1-41b4-a94e-d71ad4395cb9.flv')
                    })
                    .catch(error => {
                        console.error('Error fetching video data:', error);
                    });
        }
    }
  };
  </script>
  
  <style lang="less" scoped>
  .video {
    height: 20rem;
    video {
        width: 95%;
        height: 100%;
        margin: 0 auto;
        // margin-top: 0.5rem;
        object-fit: cover; /* 使视频填充容器并裁剪超出部分 */
      }
  }
  

  </style>
  