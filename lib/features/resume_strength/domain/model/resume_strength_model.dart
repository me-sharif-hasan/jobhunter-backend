class ResumeStrengthModel {
  final int score;
  final String reasoning;
  List<String>? improvementSuggestions;

  ResumeStrengthModel({
    required this.score,
    required this.reasoning,
    this.improvementSuggestions,
  });

  factory ResumeStrengthModel.fromJson(Map<String, dynamic> json) {
    return ResumeStrengthModel(
      score: json['score'] ?? 0,
      reasoning: json['reasoning'] ?? '',
      improvementSuggestions: (json['improvementSuggestions'] as List?)
          ?.map((item) => item.toString())
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'score': score,
      'reasoning': reasoning,
      'improvementSuggestions': improvementSuggestions,
    };
  }
}
