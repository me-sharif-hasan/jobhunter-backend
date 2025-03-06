package com.iishanto.jobhunterbackend.infrastructure.gemini;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.Builder;

public final class GeminiPromptLibrary {
    public static String getPrompt(PromptType promptType, PromptParameters build) {
        return switch (promptType) {
            case JOB_DETAIL -> getJobDetailPromptTemplate(build);
            case JOB_LIST -> getJobListPromptTemplate(build);
        };
    }

    private static String getJobDetailPromptTemplate(PromptParameters build) {
        return """
                    {
                        "contents": [{
                            "parts": [
                                {
                                    "text": "%s"
                                },
                                {
                                    "text": "See the above Markdown? It's from a job site. I am having a hard time parsing this, so I need your help. From this HTML, you need to parse the job details. I need you to extract the job details and return them in the following JSON format (Not array):
                                    {
                                        \\"jobId\\": \\"<Unique Job ID>\\",
                                        \\"title\\": \\"<Job Title>\\",
                                        \\"company\\": \\"<Company Name>\\",
                                        \\"companyWebsite\\": \\"<Company Website URL>\\",
                                        \\"jobUrl\\": \\"<Job Posting URL>\\",
                                        \\"location\\": \\"<Job Location>\\",
                                        \\"salary\\": \\"<Salary Information>\\",
                                        \\"jobType\\": \\"<Job Type>\\",
                                        \\"jobCategory\\": \\"<Job Category>\\",
                                        \\"jobDescription\\": \\"<Full Job Description>\\",
                                        \\"jobPostedDate\\": \\"<Job Posted Date>\\",
                                        \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                        \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                        \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                        \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                        \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                        \\"skillsNeeded\\": \\"<Skills Needed>\\,
                                        \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                    }
                                    Sites Base URL is: %s, use it when you need to construct the full URL. For missing values, if it is possible to guess from the title, do so else leave it blank.
                                    Ensure the JSON is valid (double quoted) and properly formatted. Silently ignore fields that are not available.
                                    If no job is found follow this format for response,
                                    {
                                        \\"error\\": \\"No job found in the given HTML\\"
                                    }
                                    .Thanks!"
                                }
                                ]
                        }],
                    }
                    """.formatted(build.escapedMessage,build.baseUrl);
    }

    public static String getJobListPromptTemplate(PromptParameters promptParameters){
        return """
                    {
                        "contents": [{
                            "parts": [
                                {
                                    "text": "%s"
                                },
                                {
                                    "text": "See the above Markdown? It's from a job site. I am having a hard time parsing this, so I need your help. From this HTML, you need to parse the list of all jobs. I need you to extract the jobs and return them in the following JSON format:
                                    [
                                        {
                                            \\"jobId\\": \\"<Unique Job ID>\\",
                                            \\"title\\": \\"<Job Title>\\",
                                            \\"company\\": \\"<Company Name>\\",
                                            \\"companyWebsite\\": \\"<Company Website URL>\\",
                                            \\"jobUrl\\": \\"<Job Posting URL>\\",
                                            \\"location\\": \\"<Job Location>\\",
                                            \\"salary\\": \\"<Salary Information>\\",
                                            \\"jobType\\": \\"<Job Type>\\",
                                            \\"jobCategory\\": \\"<Job Category>\\",
                                            \\"jobDescription\\": \\"<Full Job Description>\\",
                                            \\"jobPostedDate\\": \\"<Job Posted Date>\\",
                                            \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                            \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                            \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                            \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                            \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                            \\"skillsNeeded\\": \\"<Skills Needed>\\",
                                            \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                        },
                                        {
                                                \\"jobId\\": \\"<Unique Job ID>\\",
                                                \\"title\\": \\"<Job Title>\\",
                                                \\"company\\": \\"<Company Name>\\",
                                                \\"companyWebsite\\": \\"<Company Website URL>\\",
                                                \\"jobUrl\\": \\"<Job Posting URL>\\",
                                                \\"location\\": \\"<Job Location>\\",
                                                \\"salary\\": \\"<Salary Information>\\",
                                                \\"jobType\\": \\"<Job Type>\\",
                                                \\"jobCategory\\": \\"<Job Category>\\",
                                                \\"jobDescription\\": \\"<Full Job Description>\\",
                                                \\"jobPostedDate\\": \\"<Job Posted Date>\\",
                                                \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                                \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                                \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                                \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                                \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                                \\"skillsNeeded\\": \\"<Skills Needed>\\",
                                                \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                            },
                                        ...<more jobs>...
                                    ]
                                    Sites Base URL is: %s, use it when you need to construct the full URL. For missing values, if it is possible to guess from the title, do so else leave it blank.
                                    Ensure the JSON is valid (double quoted) and properly formatted. Silently ignore fields that are not available.
                                    For unique job id give anything that is unique for the job. Using job title is risky because it be repeat. You can choose the url of job too, or any other
                                    If you choose the url for ID, please keep it as it is. do not change it.
                                    like site__<some uniqueness>. If no job is found follow this format for response,
                                    {
                                        \\"error\\": \\"No job found in the given HTML\\"
                                    }
                                    .Thanks!"
                                }
                            ]
                        }],
                        "generationConfig": {
                            "temperature": %d
                        }
                    }
                """.formatted(promptParameters.escapedMessage,promptParameters.baseUrl,promptParameters.temperature);
    }



    @Builder
    public static class PromptParameters{
        public String escapedMessage;
        public String baseUrl;
        public int temperature;
    }

    public static enum PromptType{
        JOB_LIST,
        JOB_DETAIL
    }
}
