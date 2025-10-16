<template>
  <div>
    <div class="card" style="margin-bottom: 5px">Welcome! <b>{{ data.user?.name }}</b>, have a nice day! </div>
    <div style="margin-bottom: 10px" v-if="data.user.role === 'USER'">
      <el-row :gutter="10">
        <el-col :span="12">
          <div class="card" style="padding: 20px">
            <div id="line1" style="height: 400px"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="card" style="padding: 20px">
            <div id="line2" style="height: 400px"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div>
      <el-row :gutter="10">
        <el-col :span="12">
          <div class="card" style="height: 400px">
            <div style="font-weight: bold; font-size: 18px; padding: 10px 0 30px 10px">System Announcement</div>
            <el-timeline style="max-width: 600px">
              <el-timeline-item
                  color="orange"
                  v-for="(item, index) in data.noticeData"
                  :key="index"
                  :timestamp="item.time"
              >
                {{ item.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-col>

        <el-col :span="12" v-if="data.user.role === 'USER'">
          <div class="card" style="height: 400px">
            <div style="font-weight: bold; font-size: 18px; padding: 10px 0 30px 10px">Daily Health Reminders</div>
            <div style="padding: 20px; font-size: 16px">
              <div style="margin-bottom: 10px">Body Record:
                <span style="margin-left: 20px; color: green" v-if="data.record.body">Recorded</span>
                <span style="margin-left: 20px; color: red" v-else>Undocumented</span>
              </div>
              <div style="margin-bottom: 10px">Sports Record:
                <span style="margin-left: 20px; color: green" v-if="data.record.sports">Recorded</span>
                <span style="margin-left: 20px; color: red" v-else>Undocumented</span>
              </div>
              <div style="margin-bottom: 10px">Eating Record:
                <span style="margin-left: 20px; color: green" v-if="data.record.eating">Recorded</span>
                <span style="margin-left: 20px; color: red" v-else>Undocumented</span>
              </div>
              <div style="margin-bottom: 10px">Sleep Record:
                <span style="margin-left: 20px; color: green" v-if="data.record.sleep">Recorded</span>
                <span style="margin-left: 20px; color: red" v-else>Undocumented</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>

import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts'

const lineOption1 = {
  title: {
    text: 'Blood pressure trend in 7 days',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    name: 'Date',
    type: 'category',
    data: []
  },
  yAxis: {
    name: 'mmHg',
    type: 'value'
  },
  // grid: {
  //   top: '20%',
  //   bottom:'10%'
  // },
  series: [
    {
      name: 'LowPressure',
      data: [],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.8, // Shadow transparency 阴影的透明度
        color: 'rgb(185,190,255)' // The color and transparency of the shadow 阴影的颜色和透明度
      },
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
    {
      name: 'HighPressure',
      data: [],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.8,
        color: 'rgb(185,190,255)'
      },
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
  ]
}

const lineOption2 = {
  title: {
    text: 'Exercise duration trend in 7 days',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    name: 'Date',
    type: 'category',
    data: []
  },
  yAxis: {
    name: 'h',
    type: 'value'
  },
  grid: {
    top: '20%',
    bottom:'10%'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.8,
        color: 'rgb(185,190,255)'
      },
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
  ]
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: [],
  record: {},
})

// Wait until all elements on the page are loaded before setting the echarts chart
// 等页面所有元素加载完成后再设置 echarts图表
onMounted(() => {
  if (data.user.role === 'USER') {
    // Request data Initialize chart 请求数据  初始化图表
    // line chart
    let lineDom1 = document.getElementById('line1')
    let lineChart1 = echarts.init(lineDom1)
    request.get('/selectLine1').then(res => {
      lineOption1.xAxis.data = res.data.date || []
      lineOption1.series[0].data = res.data.low || []
      lineOption1.series[1].data = res.data.high || []
      lineChart1.setOption(lineOption1)
    })

    let lineDom2 = document.getElementById('line2')
    let lineChart2 = echarts.init(lineDom2)
    request.get('/selectLine2').then(res => {
      lineOption2.xAxis.data = res.data.date || []
      lineOption2.series[0].data = res.data.during || []
      lineChart2.setOption(lineOption2)
    })
  }
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()

const loadRecord = () => {
  request.get('/selectRecord').then(res => {
    if (res.code === '200') {
      data.record = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
if (data.user.role === 'USER') {
  loadRecord()
}
</script>