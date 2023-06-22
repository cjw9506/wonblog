<script setup lang="ts">
import { onMounted, ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
  commentId: {
    type: [Number, String],
    require: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  author: "",
  content: "",
  createdAt: new Date()
});


const comments = ref([] as any[]);


const writer = ref("");
const content = ref("");

const comment = ref({
  id: 0,
  writer: "",
  content: "",
});

const router = useRouter();

const moveToEditPost = () => {
  router.push({ name: "edit", params: { postId: props.postId } });
};

const moveToEditComment = (commentId: number | string) => {
  router.push({ name: "editComment", params: { postId: props.postId, commentId: commentId } });
};

const writeComment = function () {
  axios
    .post(`/api/posts/${props.postId}/comments`, {
      writer: writer.value,
      content: content.value,
    })
    .then(() => {
      // 댓글 작성 후에 댓글 목록을 다시 불러옴
      getComments();
    })
    .catch((error) => {
      console.error(error);
    });
};

const getComments = () => {
  axios
    .get(`/api/posts/${props.postId}/comments?page=1&size=5`)
    .then((response) => {
      comments.value = response.data;
    })
    .catch((error) => {
      console.error(error);
    });
};

onMounted(() => {
  axios
    .get(`/api/posts/${props.postId}`)
    .then((response) => {
      post.value = response.data;
    })
    .catch((error) => {
      console.error(error);
    });

  getComments();
});
</script>

<template>
  <br>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="createdAt">{{ post.createdAt }}</div>
        <div class="author">작성자 : {{ post.author }}</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEditPost()">수정</el-button>
      </div>
    </el-col>
  </el-row>

  <ul>
    <div class="comment-title">댓글 목록</div>
    <li v-for="comment in comments" :key="comment.id">

      <div class="writer">
        {{comment.writer}}
      </div>

      <div class="content">
        {{ comment.content }}
      </div>

      <div class="d-flex justify-content-end">
        <el-button type="success" @click="moveToEditComment(comment.id)">수정</el-button>
      </div>
    </li>
  </ul>

  <div class="mt-3">
      <el-input v-model="writer" placeholder="작성자" />
  </div>

  <div class="mt-2">
      <el-input v-model="content" placeholder="댓글 내용" />
  </div>

  <div class="mt-2">
      <div class="d-flex justify-content-end">
        <el-button type="primary" @click="writeComment()">작성완료</el-button>
      </div>
  </div>




</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.comment-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #383838;
  margin-bottom: 1rem;

}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .author {
      margin-left: 10px;
      color: #6b6b6b;
    }

  .createdAt {
    margin-left: 10px;
    color: #6b6b6b;
  }
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #616161;
  white-space: break-spaces;
  line-height: 1.5;
}

ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      line-height: 1.4;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 8px;
      font-size: 0.78rem;

      .createdAt {
        margin-left: 10px;
        color: #6b6b6b;
      }
      .author {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}
</style>
