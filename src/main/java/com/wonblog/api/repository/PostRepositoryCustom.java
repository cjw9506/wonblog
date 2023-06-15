package com.wonblog.api.repository;

import com.wonblog.api.domain.Post;
import com.wonblog.api.dto.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
