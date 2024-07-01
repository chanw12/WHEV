package com.ll.whev.domain.image.repository;


import com.ll.whev.domain.image.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>,CustomImageRepository  {

}
