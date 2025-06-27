package com.iishanto.jobhunterbackend.infrastructure.google;

import lombok.Builder;

import java.sql.Timestamp;
import java.time.Instant;

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
                                    "text": "Current Time: %s. Ensure consistent formatting and valid JSON.
                                    See the above Markdown? It's from a job site. I am having a hard time parsing this, so I need your help. From this HTML, you need to parse the job details. I need you to extract the job details and return them in the following JSON format (Not array):
                                    {
                                        \\"jobId\\": \\"<Unique Job ID job_unique_token_from_url> (special chars like dash, space will be underscored)\\",
                                        \\"title\\": \\"<Job Title>\\",
                                        \\"company\\": \\"<Company Name>\\",
                                        \\"companyWebsite\\": \\"<Company Website URL>\\",
                                        \\"jobUrl\\": \\"<Job Posting URL (URL where the details is available (hyperlink of the job card), not apply link), If all job url are same, make it same as job apply url>\\",
                                        \\"location\\": \\"<Job Location>\\",
                                        \\"salary\\": \\"<Salary Information>\\",
                                        \\"jobType\\": \\"<Job Type>\\",
                                        \\"jobCategory\\": \\"<Job Category>\\",
                                        \\"jobDescription\\": \\"<Full Job Description, within 2000 words or less>\\",
                                        \\"jobPostedDate\\": \\"<Job Posted Date, ensure yyyy-MM-ddTHH:mm:ssZ format, given current time>\\",
                                        \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                        \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                        \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                        \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                        \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                        \\"skillsNeeded\\": \\"<Skills Needed>\\,
                                        \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                    }
                                    Job URL is the url immediately followed by the name of the job or that makes sense. I mean in Browser using which link user navigate is job url.
                                    Sites Base URL is: %s, use it when you need to construct the full URL. For missing values, if it is possible to guess from the title, do so else leave it blank.
                                    Ensure the JSON is valid (double quoted) and properly formatted. Silently ignore fields that are not available.
                                    For linkedin or facebook sourced jobs, job url will be the post url if available.
                                    Choose Unique ID from the Job Url. If you have multiple job here, compare and take the one token that really matters. If it is not necessary do not take query parameters, but if it matters you should.
                                    If no job is found follow this format for response,
                                    {
                                        \\"error\\": \\"No job found in the given HTML\\"
                                    }
                                    .Thanks!"
                                }
                                ]
                        }],
                    }
                    """.formatted(Timestamp.from(Instant.now()),build.escapedMessage,build.baseUrl);
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
                                    "text": "Current Time: %s. Ensure consistent formatting and valid JSON.
                                    See the above Markdown? It's from a job site. I am having a hard time parsing this, so I need your help. From this HTML, you need to parse the list of all jobs. I need you to extract the jobs and return them in the following JSON format:
                                    [
                                        {
                                            \\"jobId\\": \\"<Unique Job ID, follow this format: job_unique_token_from_url> (special chars like dash, space will be underscored)\\",
                                            \\"title\\": \\"<Job Title>\\",
                                            \\"company\\": \\"<Company Name>\\",
                                            \\"companyWebsite\\": \\"<Company Website URL>\\",
                                            \\"jobUrl\\": \\"<Job Posting URL (URL where the details is available (hyperlink of the job card), not apply link), If all job url are same, make it same as job apply url>\\",
                                            \\"location\\": \\"<Job Location>\\",
                                            \\"salary\\": \\"<Salary Information>\\",
                                            \\"jobType\\": \\"<Job Type>\\",
                                            \\"jobCategory\\": \\"<Job Category>\\",
                                            \\"jobDescription\\": \\"<Full Job Description, within 50 words if job url exists else within 400 words or less, must summarize full description given, include responsibilities, context etc>\\",
                                            \\"jobPostedDate\\": \\"<Job Posted Date, ensure yyyy-MM-ddTHH:mm:ssZ format, given current time>\\",
                                            \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                            \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                            \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                            \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                            \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                            \\"skillsNeeded\\": \\"<Skills Needed>\\",
                                            \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                        },
                                        {
                                                \\"jobId\\": \\"<Unique Job ID> (special chars like dash, space will be underscored)\\",
                                                \\"title\\": \\"<Job Title>\\",
                                                \\"company\\": \\"<Company Name>\\",
                                                \\"companyWebsite\\": \\"<Company Website URL>\\",
                                                \\"jobUrl\\": \\"<ob Posting URL (URL where the details is available (hyperlink of the job card), not apply link), If all job url are same, make it same as job apply url>\\",
                                                \\"location\\": \\"<Job Location>\\",
                                                \\"salary\\": \\"<Salary Information>\\",
                                                \\"jobType\\": \\"<Job Type>\\",
                                                \\"jobCategory\\": \\"<Job Category>\\",
                                                \\"jobDescription\\": \\"<Full Job Description, within 50 words if job url exists else within 400 words or less, must summarize full description given, include responsibilities, context etc>\\",
                                                \\"jobPostedDate\\": \\"<Job Posted Date>\\",
                                                \\"jobLastDate\\": \\"<Last Date to Apply>\\",
                                                \\"jobApplyLink\\": \\"<Direct Apply Link>\\",
                                                \\"jobApplyEmail\\": \\"<Contact Email for Application>\\",
                                                \\"jobParsedAt\\": \\"<Timestamp of Parsing>\\",
                                                \\"jobUpdatedAt\\": \\"<Timestamp of Last Update>\\",
                                                \\"skillsNeeded\\": \\"<Skills Needed>\\",
                                                \\"experienceNeeded\\": \\"<Experience Needed>\\"
                                            },
                                        ...<more jobs (10 jobs max at a time)>...
                                    ]
                                    Job URL is the url immediately followed by the name of the job or that makes sense. I mean in Browser using which link user navigate is job url.
                                    Sites Base URL is: %s, use it when you need to construct the full URL. For missing values, if it is possible to guess from the title, do so else leave it blank.
                                    Ensure the JSON is valid (double quoted) and properly formatted. Silently ignore fields that are not available.
                                    For linkedin or facebook sourced jobs, job url will be the post url if available.
                                    Choose Unique ID from the Job Url. If you have multiple job here, compare and take the one token that really matters. If it is not necessary do not take query parameters, but if it matters you should.
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
                """.formatted(Timestamp.from(Instant.now()),promptParameters.escapedMessage,promptParameters.baseUrl,promptParameters.temperature);
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
