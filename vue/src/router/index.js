import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect:'/manager/home'},
    { path: '/404', component: ()=> import('@/views/404.vue')},
    { path: '/Login', component: ()=> import('@/views/Login.vue')},
    { path: '/Register', component: ()=> import('@/views/Register.vue')},
    { path: '/:pathMatch(.*)', redirect: '/404'},
    { path:'/manager',
      component:()=>import('@/views/Manager.vue'),
      children:[
        {path:'home', meta:{name:'manager homepage'}, component:()=>import('@/views/manager/Home.vue'), },
        {path:'admin', meta:{name:'Admin information'}, component:()=>import('@/views/manager/Admin.vue'), },
        {path:'notice', meta:{name:'System announcement'}, component:()=>import('@/views/manager/Notice.vue'), },
        {path:'person', meta:{name:'Personal files'}, component:()=>import('@/views/manager/Person.vue'), },
        {path:'password', meta:{name:'Change password'}, component:()=>import('@/views/manager/Password.vue'), },
        {path:'user', meta:{name:'Ordinary user information'}, component:()=>import('@/views/manager/User.vue'), },
        {path:'bodyRecords', meta:{name:'Body record information'}, component:()=>import('@/views/manager/BodyRecords.vue'), },
        {path:'sportsRecords', meta:{name:'Sports record information'}, component:()=>import('@/views/manager/SportsRecords.vue'), },
        {path:'eatingRecords', meta:{name:'Eating record information'}, component:()=>import('@/views/manager/EatingRecords.vue'), },
        {path:'sleepRecords', meta:{name:'Sleep record information'}, component:()=>import('@/views/manager/SleepRecords.vue'), },
        {path:'forum', meta:{name:'Post information'}, component:()=>import('@/views/manager/Forum.vue'), },
        {path:'globalForum', meta:{name:'Global forum'}, component:()=>import('@/views/manager/GlobalForum.vue'), },
        {path:'forumDetail', meta:{name:'Forum detail'}, component:()=>import('@/views/manager/ForumDetail.vue'), },
        {path:'comment', meta:{name:'Comment information'}, component:()=>import('@/views/manager/Comment.vue'), },
      ]
    },
    { path:'/front',
      component:()=>import('@/views/front.vue'),
      children:[
        {path:'home', component:()=>import('@/views/front/Home.vue'), },
        {path:'person', component:()=>import('@/views/front/Person.vue'), }
      ]
    }
  ],
})

export default router
