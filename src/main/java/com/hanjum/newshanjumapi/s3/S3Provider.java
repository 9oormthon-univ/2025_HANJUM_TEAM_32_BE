package com.hanjum.newshanjumapi.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Provider {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            // TODO: 비어있는 파일에 대한 예외 처리
            return null;
        }

        String fileName = createFileName(file);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return getFileUrl(fileName);
        } catch (IOException e) {
            log.error("S3 파일 업로드 중 입출력 예외가 발생했습니다.", e);
            // TODO: 업로드 실패에 대한 예외 처리
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.", e);
        }
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String key = getKeyFromUrl(fileUrl);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3에서 파일 삭제 성공: {}", key);
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 예외가 발생했습니다. URL: {}", fileUrl, e);
            // TODO: 삭제 실패에 대한 예외 처리
        }
    }

    private String createFileName(MultipartFile file) {
        return dir + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    }

    private String getFileUrl(String filename) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(filename)
                .build();
        URL url = s3Client.utilities().getUrl(getUrlRequest);
        return url.toString();
    }

    private String getKeyFromUrl(String fileUrl) {
        String baseUrl = "https://" + bucket + ".s3.";
        int beginIndex = fileUrl.indexOf(baseUrl);
        if (beginIndex == -1) {
            log.warn("S3 URL에서 기본 형식을 찾지 못했습니다: {}", fileUrl);
            try {
                URL url = new URL(fileUrl);
                return url.getPath().substring(1);
            } catch (Exception e) {
                return null;
            }
        }
        return fileUrl.substring(fileUrl.indexOf("/", beginIndex + baseUrl.length()) + 1);
    }
}