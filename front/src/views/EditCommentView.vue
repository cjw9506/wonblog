<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const comment = ref({
  writer: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
  commentId: {
    type: [Number, String],
    required: true,
  },
});

axios
  .get(`/api/posts/${props.postId}/comments/${props.commentId}`)
  .then((response) => {
    comment.value = response.data;
  })
  .catch((error) => {
    console.error(error);
  });

const editComment = () => {
  axios
    .patch(`http://13.209.214.190:8080/api/posts/${props.postId}/comments/${props.commentId}`, comment.value)
    .then(() => {
          router.push({ name: "read", params: { postId: props.postId } });
    })
};
</script>

<template>
  <br>
  <div class="mt-2">
    <el-input v-model="comment.writer" />
  </div>

  <div class="mt-2">
    <el-input v-model="comment.content" type="textarea" rows="15" />
  </div>

  <div class="mt-2 d-flex justify-content-end">
    <el-button type="warning" @click="editComment">수정</el-button>
  </div>
</template>

<style></style>
