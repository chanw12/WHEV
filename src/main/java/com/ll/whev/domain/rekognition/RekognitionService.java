package com.ll.whev.domain.rekognition;

import com.ll.whev.global.app.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    @Autowired
    private RekognitionClient rekognitionClient;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<TranslatedLabel> detectLabelsInImage(MultipartFile multipartFile) throws IOException {
        ByteBuffer imageBytes = ByteBuffer.wrap(multipartFile.getBytes());

        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(Image.builder()
                        .bytes(SdkBytes.fromByteBuffer(imageBytes))
                        .build())
                .maxLabels(10)
                .build();

        DetectLabelsResponse result = rekognitionClient.detectLabels(request);

        // 레이블 이름을 한국어로 번역
        return result.labels().stream()
                .map(label -> new TranslatedLabel(
                        translateText(label.name()),
                        label.confidence(),
                        label.instances(),
                        label.parents().stream()
                                .map(parent -> new TranslatedParent(translateText(parent.name())))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private String translateText(String text) {
        String url = String.format("https://translation.googleapis.com/language/translate/v2?q=%s&target=ko&key=%s", text, AppConfig.getGoogleAPIkey());
        GoogleTranslateResponse response = restTemplate.getForObject(url, GoogleTranslateResponse.class);
        return response.getData().getTranslations().get(0).getTranslatedText();
    }

    public static class TranslatedLabel {
        private final String name;
        private final Float confidence;
        private final List<?> instances;
        private final List<TranslatedParent> parents;

        public TranslatedLabel(String name, Float confidence, List<?> instances, List<TranslatedParent> parents) {
            this.name = name;
            this.confidence = confidence;
            this.instances = instances;
            this.parents = parents;
        }

        // getters
        public String getName() {
            return name;
        }

        public Float getConfidence() {
            return confidence;
        }

        public List<?> getInstances() {
            return instances;
        }

        public List<TranslatedParent> getParents() {
            return parents;
        }
        @Override
        public String toString() {
            return "TranslatedLabel{" +
                    "name='" + name + '\'' +
                    ", confidence=" + confidence +
                    ", instances=" + instances +
                    ", parents=" + parents +
                    '}';
        }

    }

    public static class TranslatedParent {
        private final String name;

        public TranslatedParent(String name) {
            this.name = name;
        }

        // getters
        public String getName() {
            return name;
        }


    }

    public static class GoogleTranslateResponse {
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public static class Data {
            private List<Translation> translations;

            public List<Translation> getTranslations() {
                return translations;
            }

            public void setTranslations(List<Translation> translations) {
                this.translations = translations;
            }
        }

        public static class Translation {
            private String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }

            public void setTranslatedText(String translatedText) {
                this.translatedText = translatedText;
            }
        }
    }
}
