import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'package:personalized_job_hunter/features/common/widgets/modern_loader.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/resume_score_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/reasoning_card_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/improvement_suggestions_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/resume_stats_widget.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class ResumeStrengthScreen extends StatefulWidget {
  final Job job;
  const ResumeStrengthScreen({super.key, required this.job});

  @override
  State<ResumeStrengthScreen> createState() => _ResumeStrengthScreenState();
}

class _ResumeStrengthScreenState extends State<ResumeStrengthScreen> {
  Map<String, String>? resumeStrengthResponse;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadResumeAnalysis();
  }

  void _loadResumeAnalysis() {
    Future.delayed(const Duration(seconds: 3), () {
      if (mounted) {
        setState(() {
          resumeStrengthResponse = <String, String>{};
          resumeStrengthResponse!['score'] = '70';
          resumeStrengthResponse!['category'] = "Good";
          resumeStrengthResponse!['reasoning'] = 
              "Your resume shows good alignment with the job requirements. "
              "The technical skills section matches well with the position, "
              "and your experience demonstrates relevant expertise. "
              "However, there are opportunities to strengthen your application "
              "by adding more specific achievements and quantifiable results.";
          isLoading = false;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade50,
      appBar: AppBar(
        title: const Text(
          'Resume Strength',
          style: TextStyle(
            fontWeight: FontWeight.w600,
            color: Colors.white,
          ),
        ),
        backgroundColor: Constants.getThemeColor(1)[0],
        elevation: 0,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: isLoading ? _buildLoadingState() : _buildContent(),
    );
  }

  Widget _buildLoadingState() {
    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [const Color.fromARGB(255, 255, 255, 255), const Color.fromARGB(255, 238, 238, 238)],
        ),
      ),
      child: const Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ModernLoader(size: 80),
            SizedBox(height: 24),
            Text(
              'Analyzing your resume...',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w500,
                color: Color.fromARGB(255, 114, 114, 114),
              ),
            ),
            SizedBox(height: 8),
            Text(
              'This may take a few moments',
              style: TextStyle(
                fontSize: 14,
                color: Color.fromARGB(179, 116, 116, 116),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildContent() {
    final score = int.parse(resumeStrengthResponse!['score']!);
    final category = resumeStrengthResponse!['category']!;
    final reasoning = resumeStrengthResponse!['reasoning']!;
    
    return SingleChildScrollView(
      child: Column(
        children: [
          const SizedBox(height: 16),
          
          // Header with job info
          ResumeStatsWidget(
            score: score,
            jobTitle: widget.job.title,
            companyName: widget.job.company ?? 'Company',
          ),
          
          const SizedBox(height: 24),
          
          // Score display
          ResumeScoreWidget(
            score: score,
            category: category,
            size: 180,
          ),
          
          const SizedBox(height: 24),
          
          // Analysis reasoning
          ReasoningCardWidget(
            reasoning: reasoning,
            title: 'Analysis',
          ),
          
          // Improvement suggestions
          ImprovementSuggestionsWidget(
            suggestions: ImprovementSuggestionsWidget.getDefaultSuggestions(score),
          ),
          
          const SizedBox(height: 32),
        ],
      ),
    );
  }
}