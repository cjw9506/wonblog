<script setup lang="ts">
import { ref } from "vue";

import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  author: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`http://13.209.214.190:8080/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`http://13.209.214.190:8080/posts/${props.postId}`, post.value).then(() => {
    router.replace({ name: "home" });
  });
};
</script>

<template>
  <br>
  <div>
    <el-input v-model="post.title" />
  </div>

  <div class="mt-2">
      <el-input v-model="post.author" />
    </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15" />
  </div>

  <div class="mt-2 d-flex justify-content-end">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>
</template>

<style></style>
