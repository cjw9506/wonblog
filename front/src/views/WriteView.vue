<script setup lang="ts">
import { ref } from "vue";

import axios from "axios";
import { useRouter } from "vue-router";

const title = ref("");
const author = ref("");
const content = ref("");

const router = useRouter();

const write = function () {
  axios
    .post("http://13.209.214.190:8080/posts", {
      title: title.value,
      author: author.value,
      content: content.value,

    })
    .then(() => {
      router.replace({ name: "home" });
    });
};
</script>

<template>
  <br>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
  </div>

  <div class="mt-2">
    <el-input v-model="author" placeholder="작성자" />
  </div>

  <div class="mt-2">
      <el-input v-model="content" type="textarea" rows="15" placeholder="내용을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
  </div>
</template>

<style></style>
