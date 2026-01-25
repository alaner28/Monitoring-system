<template>
    <div class="panel">
        <div class="title">最新报警列表</div>
        <div id="demoDiv">
            <div v-for="(item, index) in alarmlist" 
            :key="item.id" class="itemlist" 
            :style="itemlist1[getcolor(item.eventName)]"
            @click="showDetail(item)">
                <div class="itemlist">
                    <h3>{{ index + 1 }} {{ item.eventName }} -- {{ item.department }}</h3>
                    <img :src=" item.checked ? require('../../public/assets/checked.png') : require('../../public/assets/unchecked.png')" alt="">
                </div>
            </div>
        </div>
        <dialog1 v-if="dialogVisible1" :item="item" @updateDialogVisible1="handleDialogVisibility"></dialog1>

        <div class="panel-footer"></div>
    </div>
</template>

<script>
import dialog1 from './dialog1.vue';
export default {
    name: 'alarmlist-1',
    components:{
        dialog1
    },
    data() {
        return {
            websocket: null,  // 用于存储 WebSocket 实例
            dialogVisible1:false,
            item:'',
            alarmlist: [],  
            itemlist1: [
                {
                    // 挥手
                    'backgroundColor': '#F1948A'
                },
                {
                    // 摔倒
                    'backgroundColor': '#F8BD91'
                },
                {
                    // 吸烟
                    'backgroundColor': '#ffd9d9'
                },
                {
                    // 进入危险区域
                    'backgroundColor': '#7ABDD8'
                },
                {
                    // 打拳
                    'backgroundColor': '#F1948A'
                },
                {
                    // 明火
                    'backgroundColor': '#e7e3fe'
                },
                {
                    // 区域停留
                    'backgroundColor': '#2CD6DB'
                },
                {
                    // 吸烟
                    'backgroundColor': '#C4D83B'
                },
                {
                    // 否则
                    'backgroundColor': '#7ABDD8'
                }
            ],
            pageNum:1,
            pageSize:30
        };
    },
    methods: {
        getcolor(type) {
            if (type === '挥手') return 3;
            else if (type === '摔倒') return 1;
            else if (type === '烟雾') return 2;
            else if (type === '进入危险区域') return 3;
            else if (type === '打拳') return 4;
            //明火
            else if (type === '明火') return 5;
            else if(type === '区域停留') return 6;
            else if(type === '吸烟') return 7;
            else return 8;
        },

        showDetail(item){
            this.dialogVisible1 = true
            console.log('item',item);
            this.item = item
            
        },

        handleDialogVisibility(res) {
            this.dialogVisible1 = res;
        },


        // 获取报警列表
        fetchAlarmList() {
            const data = {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                status: 0,
            };
            
            this.$axios({
                methos:'GET',
                url:'http://8.152.219.117:10215/api/v1/alarm/query',
                params:data

                // url:'four/data',
                
            }).then(response => {
                    // console.log('收到报警查询数据',response.data.data);
                    
                    const newAlarmList = response.data.data.alarmList;
                    if (newAlarmList.length > 0 && JSON.stringify(newAlarmList) !== JSON.stringify(this.alarmlist)) {
                        
                        if(this.alarmlist.length > 0){
                            this.$bus.$emit('alarm');  // 触发事件总线'alarm'事件
                            this.$message({
                                message: '您有一条报警新消息',
                                type: 'warning'
                            });
                        } 
                        this.alarmlist = newAlarmList;  // 更新报警列表
                        
                    }
                 
                })
                .catch(error => {
                    console.log('报警数据查询失败');
                    
                    console.log('Error fetching alarm list:', error);
                });
        }
    },
    mounted() {
        this.fetchAlarmList();  // 初始获取报警列表
        
        setInterval(() => {
            this.fetchAlarmList();  // 每秒更新报警列表
        }, 1000);
    },
};
</script>

<style lang="less" scoped>
#demoDiv {
    width: 80%;
    height: 90%;
    margin-left: 2.2rem;
    margin-top: 1rem;
    overflow: auto;
}

#demoDiv::-webkit-scrollbar {
    width: 0;
    height: 0;
}

.itemlist {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 2.8rem;
    margin-top: 0.8rem;
    margin-bottom: 1.5rem;
    border-radius: 0.8rem;
    text-align: left;
    box-sizing: border-box;
    padding-top: 0.2rem;
    cursor: pointer;
    
    h3 {
        margin-left: 1.2rem;
        font-size: 1.6rem;
        display: inline-block;
        white-space: nowrap; /* 不换行 */
        overflow: hidden; /* 溢出隐藏 */
        text-overflow: ellipsis; /* 溢出用省略号表示 */
    }

    img {
        margin-top: 0.2rem;
        margin-right: 1rem;
        width: 2rem;
        height: 2rem;
    }
}

.panel {
    color: white;
    display: flex;
}

.title {
    width: 2rem;
    font-size: 1.8rem;
    padding-top: 1.2rem;
    font-weight: 600;
}
</style>
