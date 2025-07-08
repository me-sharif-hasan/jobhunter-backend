package com.iishanto.jobhunterbackend.domain.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class SiteAttributeValidatorModel {
    String url;
    Long siteId;
    List <JobExtractionPipeline> processFlow;
    String titleScript;
    String jobListingScript;


    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "operation"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ClickOnElement.class, name = "click"),
            @JsonSubTypes.Type(value = NavigateToOpenedPage.class, name = "navigate"),
            @JsonSubTypes.Type(value = FindElements.class, name = "find"),
            @JsonSubTypes.Type(value = MapElementResult.class, name = "map"),
            @JsonSubTypes.Type(value = BackToPreviousPage.class, name = "back"),
            @JsonSubTypes.Type(value = SaveJob.class, name = "save"),
            @JsonSubTypes.Type(value = AskAI.class, name = "askAi")
    })
    @NoArgsConstructor
    @Data
    public static class JobExtractionPipeline {
        private String operation;
        public JobExtractionPipeline(String operation) {
            this.operation = operation;
        }
    }

    @Getter
    @Setter
    public static class ClickOnElement extends JobExtractionPipeline {
        private String selector;
        public ClickOnElement() {
            super("click");
        }
    }

    @Getter
    @Setter
    public static class NavigateToOpenedPage extends JobExtractionPipeline {
        String listenFor;
        public NavigateToOpenedPage() {
            super("navigate");
        }
    }

    @Getter
    @Setter
    public static class FindElements extends JobExtractionPipeline {
        private String selector;
        private List<JobExtractionPipeline> childPipelines;
        private List<MapElementResult> metaFieldsMapping;
        public FindElements() {
            super("find");
            this.selector = "find";
        }
    }

    @Getter
    @Setter
    public static class MapElementResult extends JobExtractionPipeline {
        private String selector;
        private String javaScript;
        private String attribute;
        public MapElementResult() {
            super("map");
        }
    }

    @Getter
    @Setter
    public static class SaveJob extends JobExtractionPipeline {
        public SaveJob() {
            super("save");
        }
    }

    @Getter
    @Setter
    public static class BackToPreviousPage extends JobExtractionPipeline {
        public BackToPreviousPage() {
            super("back");
        }
    }

    @Getter
    @Setter
    public static class AskAI extends JobExtractionPipeline {
        private String withContext;
        public AskAI() {
            super("askAi");
        }
    }
}
