import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'package:personalized_job_hunter/features/common/widgets/modern_loader.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/resume_score_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/reasoning_card_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/improvement_suggestions_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/widgets/resume_stats_widget.dart';
import 'package:personalized_job_hunter/features/resume_strength/controller/resume_strength_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

class ResumeStrengthScreen extends StatefulWidget {
  final Job job;
  const ResumeStrengthScreen({super.key, required this.job});

  @override
  State<ResumeStrengthScreen> createState() => _ResumeStrengthScreenState();
}

class _ResumeStrengthScreenState extends State<ResumeStrengthScreen> {
  @override
  void initState() {
    super.initState();
    _loadResumeAnalysis();
  }

  void _loadResumeAnalysis() {
    final controller = Provider.of<ResumeStrengthController>(context, listen: false);
    controller.getResumeStrength(widget.job.jobId);
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ResumeStrengthController>(
      builder: (context, controller, _) {
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
          body: controller.isLoading 
              ? _buildLoadingState() 
              : controller.errorMessage != null
                  ? _buildErrorState(controller.errorMessage!)
                  : _buildContent(controller),
        );
      },
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

  Widget _buildErrorState(String errorMessage) {
    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            Colors.red.shade400,
            Colors.red.shade600,
          ],
        ),
      ),
      child: Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.error_outline,
                size: 80,
                color: Colors.white,
              ),
              const SizedBox(height: 24),
              const Text(
                'Oops! Something went wrong',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              Text(
                errorMessage.replaceAll('Exception: ', ''),
                style: const TextStyle(
                  fontSize: 16,
                  color: Colors.white70,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 32),
              ElevatedButton.icon(
                onPressed: () {
                  final controller = Provider.of<ResumeStrengthController>(context, listen: false);
                  controller.getResumeStrength(widget.job.jobId);
                },
                icon: const Icon(Icons.refresh),
                label: const Text('Try Again'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.white,
                  foregroundColor: Colors.red.shade600,
                  padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildContent(ResumeStrengthController controller) {
    if (controller.resumeStrength == null) {
      return const Center(child: Text('No data available'));
    }

    final resumeData = controller.resumeStrength!;
    final score = resumeData.score;
    final reasoning = resumeData.reasoning;
    
    // Determine category based on score
    String category;
    if (score >= 80) {
      category = 'Excellent';
    } else if (score >= 70) {
      category = 'Good';
    } else if (score >= 60) {
      category = 'Average';
    } else {
      category = 'Needs Work';
    }
    
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