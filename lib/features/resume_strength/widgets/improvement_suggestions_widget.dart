import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/resume_strength/domain/model/resume_strength_model.dart';

class ImprovementSuggestionsWidget extends StatelessWidget {
  final ResumeStrengthModel? resumeStrengthModel;
  final List<String>? customSuggestions;

  const ImprovementSuggestionsWidget({
    super.key,
    this.resumeStrengthModel,
    this.customSuggestions,
  });

  // Default suggestions based on score
  static List<String> getDefaultSuggestions(int score) {
    if (score >= 80) {
      return [
        'Your resume is excellent! Consider minor formatting refinements',
        'Add quantifiable achievements to strengthen impact',
        'Ensure all contact information is up to date',
      ];
    } else if (score >= 70) {
      return [
        'Add more relevant keywords from the job description',
        'Include specific metrics and achievements',
        'Optimize the summary section for better impact',
        'Consider adding relevant certifications',
      ];
    } else if (score >= 60) {
      return [
        'Restructure content to match job requirements better',
        'Add more industry-specific keywords',
        'Include quantifiable results in experience section',
        'Improve formatting and visual appeal',
        'Add a compelling professional summary',
      ];
    } else {
      return [
        'Major restructuring needed to align with job requirements',
        'Add relevant skills and keywords',
        'Include work experience with measurable outcomes',
        'Improve overall formatting and structure',
        'Consider professional resume review',
        'Add education and certifications section',
      ];
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.08),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: const Color(0xFF42A5F5).withOpacity(0.1),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Icon(
                  Icons.lightbulb_outline,
                  color: Color(0xFF42A5F5),
                  size: 20,
                ),
              ),
              const SizedBox(width: 12),
              const Text(
                'Improvement Suggestions',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF333333),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          ..._buildSuggestionsList(),
        ],
      ),
    );
  }

  List<Widget> _buildSuggestionsList() {
    final suggestions = resumeStrengthModel?.improvementSuggestions;
    
    // If no suggestions from API, show "no suggestions" message
    if (suggestions == null || suggestions.isEmpty) {
      return [
        Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.grey.shade50,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.grey.shade200),
          ),
          child: Row(
            children: [
              Icon(
                Icons.info_outline,
                color: Colors.grey.shade600,
                size: 20,
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  'No specific suggestions available at this time.',
                  style: TextStyle(
                    fontSize: 15,
                    color: Colors.grey.shade600,
                    fontStyle: FontStyle.italic,
                  ),
                ),
              ),
            ],
          ),
        ),
      ];
    }

    // Build suggestion list from API data
    return suggestions.asMap().entries.map((entry) {
      String suggestion = entry.value;
      return Padding(
        padding: const EdgeInsets.only(bottom: 12),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              margin: const EdgeInsets.only(top: 6),
              width: 6,
              height: 6,
              decoration: BoxDecoration(
                color: const Color(0xFF42A5F5),
                borderRadius: BorderRadius.circular(3),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Text(
                suggestion,
                style: TextStyle(
                  fontSize: 15,
                  height: 1.4,
                  color: Colors.grey.shade700,
                ),
              ),
            ),
          ],
        ),
      );
    }).toList();
  }
}
