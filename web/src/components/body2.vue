<template>
  <div class="no">
        <div class="no-hd">
            <ul>
                <li>{{ lastmonth }}</li>
                <li>{{ lastyear }}</li>
            </ul>
            </div>
            <div class="no-bd">
            <ul>
                <li>近30天危险警报</li>
                <li>近一年危险警报</li>
            </ul>
        </div>
        <map1></map1>
    </div>
</template>

<script>
import map1 from './map1.vue';
    export default {
        name:'body-2',
        components:{
            map1
        },
        data() {
          return {
            lastmonth:235,
            lastyear:1328
          }
        },
        mounted(){
          this.updateCount();
          this.$bus.$on('alarm',()=>{
            console.log('body2接收到报警信息，准备更新');
            
            this.updateCount();
          })
        },
        beforeDestroy(){
            this.$bus.$off('alarm')
        },
        methods:{
          updateCount(){

            let config = {
              method: 'get',
              url: '/api/data-visualization/v1/statistics/thirty-days',
              headers: { 
                  'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
              }
            };

            // 使用箭头函数以保持 this 的上下文
            this.$axios(config)
              .then(response => {

                // console.log(result)
                const res = response.data;
                  if(res.code == '200'){
                    console.log('获取近30天信息成功',res.data);
                    let data = res.data
                    let cnt = 0;
                    for(let item in data){
                      
                      cnt += data[item].eventCount;
                    }
                    this.lastmonth = cnt
                    // console.log(cnt);
                    
                    
                  }
              })
              .catch(error => {
                console.log(error);
              });


              config = {
                method: 'get',
                url: '/api/data-visualization/v1/statistics/year',
                headers: { 
                    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
                }
              };

            // 使用箭头函数以保持 this 的上下文
            this.$axios(config)
              .then(response => {

                // console.log(result)
                const res = response.data;
                if(res.code == '200'){
                    console.log('获取近一年信息成功',res.data);
                    let data = res.data
                    let cnt = 0;
                    for(let item in data){
                      // console.log(('item',item));
                      
                      cnt += data[item].eventCount;
                    }
                    this.lastyear = cnt
                    // console.log(cnt);
                    
                    
                  }
              })
              .catch(error => {
                console.log(error);
              });
          }
        }
    }
</script>

<style>
@font-face {
  font-family: electronicFont;
  src: url(../../public/assets/font/DS-DIGIT.TTF);
}
.no {
  background: rgba(101, 132, 226, 0.1);
  padding: .9375rem;
  /* width: 49rem; */
  width: 40%;
}
.no .no-hd {
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
}
.no .no-hd::before {
  content: "";
  position: absolute;
  width: 1.875rem;
  height: .625rem;
  border-top: 2px solid #02a6b5;
  border-left: 2px solid #02a6b5;
  top: 0;
  left: 0;
}
.no .no-hd::after {
  content: "";
  position: absolute;
  width: 1.875rem;
  height: .625rem;
  border-bottom: 2px solid #02a6b5;
  border-right: 2px solid #02a6b5;
  right: 0;
  bottom: 0;
}
.no .no-hd ul {
  display: flex;
}
.no .no-hd ul li {
    list-style: none;
  position: relative;
  flex: 1;
  text-align: center;
  height: 5rem;
  line-height: 5rem;
  font-size: 2.625rem;
  color: #ffeb7b;
  padding: 1.25rem 0;
  /* font-family: electronicFont; */
  
   /* font-family: 'Roboto', sans-serif ; */
   /* 使用数字字体 */
   font-family: 'electronicFont'; 
    /* color: #B0E0E6; 设置文字颜色为浅蓝色 */
    font-size: 4rem; /*设置字体大小*/
    letter-spacing: 0.1rem; /*设置字母间距，模拟数字的显示效果*/
  /* font-weight: bold; */
}
.no .no-hd ul li:first-child::after {
  content: "";
  position: absolute;
  height: 50%;
  width: 5px;
  background: rgba(255, 255, 255, 0.2);
  right: 0;
  top: 25%;
}
.no .no-bd ul {
  display: flex;
}
.no .no-bd ul li {
    list-style: none;
  flex: 1;
  height: 2.5rem;
  line-height: 2.5rem;
  text-align: center;
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.7);
  padding-top: .625rem;
  
}
</style>