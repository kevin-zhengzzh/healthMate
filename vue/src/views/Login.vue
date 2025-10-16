<template>
  <div class="login-container">
    <div style="text-align: center; position: fixed; top: 50px; font-size: 40px; color: white; font-weight: bold ">HealthMate</div>
     <div class="login-box" style="position: relative">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">Login</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="please insert account number"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="please insert password"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role">
            <el-option value="ADMIN" label="Admin"></el-option>
            <el-option value="USER" label="Ordinary User"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">Login</el-button>
        </el-form-item>
        <div style="text-align: right">
          Don't have an account？Please go to <a style="color: #0066bc" href="/register">register</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: { role: 'ADMIN' },
  rules: {
    username: [
      { required: true, message: 'Please insert account number', trigger: 'blur' }
    ],
    password: [
      { required: true, message: 'Please insert password', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // form validation pass
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Login successfully')
          // storage user information
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          router.push('/manager/home')
        }else{
          ElMessage.error(res.msg || 'fail to login')
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.png");
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}
</style>