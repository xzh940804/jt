package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


@Repository
public interface FileService {
    ImageVO uploadFile(MultipartFile uploadFile);
}
