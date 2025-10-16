<template>
  <div class="manager-container">
<!--header-->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/heart.png" alt="">
        <div class="title">HealthMate</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">homepage</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor:pointer">
        <div style="padding-right: 20px; display: flex; align-items: center">
          <img
              :src="avatarSrc"
              :key="avatarSrc"
              alt=""
              style="width:40px;height:40px;border-radius:50%;display:block"
          />
          <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon color="#fff"><arrow-down /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
          <el-dropdown-item @click="router.push('/manager/person')">user information</el-dropdown-item>
          <el-dropdown-item @click="router.push('/manager/password')">change password</el-dropdown-item>
          <el-dropdown-item @click="logout">logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
        </el-dropdown>
      </div>
    </div>
<!--main-->
      <div style="display: flex">
        <div class="manager-main-left">
          <el-menu router
                   :default-openeds="['1','2','3']"
                   :default-active="router.currentRoute.value.path">

            <el-menu-item index="/manager/home">
              <el-icon><HomeFilled /></el-icon>
              <span>Homepage</span>
            </el-menu-item>
            <el-menu-item index="/manager/globalForum">
              <el-icon><Apple /></el-icon>
              <span>Health forum</span>
            </el-menu-item>
            <el-sub-menu index="1">
              <template #title>
                <el-icon><Notebook /></el-icon>
                <span>Health record</span>
              </template>
              <el-menu-item index="/manager/bodyRecords">Body record information</el-menu-item>
              <el-menu-item index="/manager/sportsRecords">Sports record information</el-menu-item>
              <el-menu-item index="/manager/eatingRecords">Eating record information</el-menu-item>
              <el-menu-item index="/manager/sleepRecords">Sleep record information</el-menu-item>
<!--              <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">System announcement</el-menu-item>-->
            </el-sub-menu>

            <el-sub-menu index="3">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>Information management</span>
              </template>
              <el-menu-item index="/manager/forum">Forum information</el-menu-item>
              <el-menu-item index="/manager/comment">Comment information</el-menu-item>
              <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">System announcement</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>User manage</span>
              </template>
              <el-menu-item index="/manager/admin">Administrator information</el-menu-item>
              <el-menu-item index="/manager/user">Ordinary user information</el-menu-item>
            </el-sub-menu>

          </el-menu>
        </div>

        <div class="manager-main-right">
          <RouterView @updateUser="updateUser" />
        </div>
      </div>

    </div>
</template>
<script setup>
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import { reactive, ref, computed, onMounted, onBeforeUnmount } from 'vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})
const logout = () => {
  //remove storage
  localStorage.removeItem("xm-user")
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const version = ref(0);
const fallback = new URL('@/assets/imgs/heart.png', import.meta.url).href;
const avatarSrc = computed(() => {
  const url = data.user?.avatar || fallback;
  return `${url}${url.includes('?') ? '&' : '?'}t=${version.value}`;
});

const refreshUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}');
  version.value = Date.now();   // 关键：强制重新加载图片
};

onMounted(() => {
  window.addEventListener('avatar-updated', refreshUser); // 监听自定义事件
});
onBeforeUnmount(() => {
  window.removeEventListener('avatar-updated', refreshUser);
});

if(!data.user.id){
  logout()
  ElMessage.error("please login")
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>