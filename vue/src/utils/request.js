import axios from "axios"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000
})

// request interceptors
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    //add token in axios request head
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    config.headers['token'] = user.token || ''

    return config
}, error => {
    return Promise.reject(error)
});

// response interceptors
// deal with response result
request.interceptors.response.use(
    response => {
        let res = response.data;
        // return files
        if (response.config.responseType === 'blob') {
            return res
        }
        // give notice when certification is not pass
        if(res.code === '401') {
            ElMessage.error(res.msg)
            router.push('/login')
        }
        // return string data
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('can not find request interface')
        } else if (error.response.status === 500) {
            ElMessage.error('system error, check backend')
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request