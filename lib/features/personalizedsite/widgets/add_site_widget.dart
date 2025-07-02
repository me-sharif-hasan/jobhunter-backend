import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';

class AddSiteWidget extends StatefulWidget {
  final Future<void> Function(String homepage, String careerPage) onSubmit;

  const AddSiteWidget({
    super.key,
    required this.onSubmit,
  });

  @override
  State<AddSiteWidget> createState() => _AddSiteWidgetState();

  static Future<void> show(
    BuildContext context,
    Future<void> Function(String homepage, String careerPage) onSubmit,
  ) {
    return showDialog(
      context: context,
      barrierDismissible: true,
      builder: (context) => AddSiteWidget(onSubmit: onSubmit),
    );
  }
}

class _AddSiteWidgetState extends State<AddSiteWidget>
    with SingleTickerProviderStateMixin {
  final TextEditingController _homepageController = TextEditingController();
  final TextEditingController _careerPageController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  late AnimationController _scaleController;
  late Animation<double> _scaleAnimation;
  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();
    _scaleController = AnimationController(
      duration: const Duration(milliseconds: 200),
      vsync: this,
    );
    _scaleAnimation = Tween<double>(
      begin: 0.8,
      end: 1.0,
    ).animate(CurvedAnimation(
      parent: _scaleController,
      curve: Curves.easeOutBack,
    ));
    _scaleController.forward();
  }

  @override
  void dispose() {
    _homepageController.dispose();
    _careerPageController.dispose();
    _scaleController.dispose();
    super.dispose();
  }

  void _closeWidget() {
    Navigator.of(context).pop();
  }

  void _handleSubmit() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isSubmitting = true;
      });
      
      try {
        await widget.onSubmit(
          _homepageController.text.trim(),
          _careerPageController.text.trim(),
        );
        
        if (mounted) {
          _closeWidget();
        }
      } catch (error) {
        if (mounted) {
          setState(() {
            _isSubmitting = false;
          });
          
          // Show error message
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(
                error.toString().replaceAll('Exception: ', ''),
                style: const TextStyle(color: Colors.white),
              ),
              backgroundColor: const Color(0xFFEF4444), // Red color
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              duration: const Duration(seconds: 4),
            ),
          );
        }
      }
    }
  }

  String? _validateUrl(String? value) {
    if (value == null || value.trim().isEmpty) {
      return 'This field is required';
    }
    
    final urlPattern = RegExp(
      r'^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)$',
    );
    
    if (!urlPattern.hasMatch(value.trim())) {
      return 'Please enter a valid URL';
    }
    
    return null;
  }

  @override
  Widget build(BuildContext context) {
    final themeColors = Provider.of<MetaController>(context).getThemeColor();
    
    return Dialog(
      backgroundColor: Colors.transparent,
      insetPadding: const EdgeInsets.all(16),
      child: ScaleTransition(
        scale: _scaleAnimation,
        child: Container(
          padding: const EdgeInsets.all(24),
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [
                Colors.white,
                Color(0xFFF8F9FA), // Very light grey-blue
              ],
            ),
            borderRadius: BorderRadius.circular(24),
            boxShadow: [
              BoxShadow(
                color: themeColors[0].withOpacity(0.2),
                blurRadius: 30,
                offset: const Offset(0, 15),
                spreadRadius: 0,
              ),
              BoxShadow(
                color: Colors.black.withOpacity(0.08),
                blurRadius: 10,
                offset: const Offset(0, 5),
              ),
            ],
            border: Border.all(
              color: themeColors[0].withOpacity(0.1),
              width: 1.5,
            ),
          ),
          child: Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Header
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Add New Site',
                          style: TextStyle(
                            fontSize: 26,
                            fontWeight: FontWeight.w700,
                            color: Color(0xFF2D3748), // Dark blue-grey
                            letterSpacing: -0.5,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Container(
                          height: 4,
                          width: 60,
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              colors: [themeColors[0], themeColors[1]],
                            ),
                            borderRadius: BorderRadius.circular(2),
                          ),
                        ),
                      ],
                    ),
                    Container(
                      decoration: BoxDecoration(
                        color: Color(0xFFF7FAFC), // Light grey background
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: Color(0xFFE2E8F0),
                          width: 1,
                        ),
                      ),
                      child: IconButton(
                        onPressed: _closeWidget,
                        icon: Icon(
                          Icons.close_rounded,
                          color: Color(0xFF64748B), // Medium grey
                          size: 22,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      colors: [
                        themeColors[0].withOpacity(0.08),
                        themeColors[1].withOpacity(0.06),
                      ],
                    ),
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(
                      color: themeColors[0].withOpacity(0.15),
                      width: 1,
                    ),
                  ),
                  child: Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(
                          color: themeColors[0].withOpacity(0.15),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Icon(
                          Icons.info_outline_rounded,
                          color: themeColors[0],
                          size: 20,
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Text(
                          'Add a company career page to monitor for new job postings',
                          style: TextStyle(
                            fontSize: 14,
                            color: Color(0xFF4A5568), // Medium dark grey
                            height: 1.4,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 24),
                
                // Homepage URL Field
                _buildInputField(
                  controller: _homepageController,
                  label: 'Company Homepage',
                  hint: 'https://company.com',
                  icon: Icons.home_rounded,
                  validator: _validateUrl,
                  themeColors: themeColors,
                ),
                const SizedBox(height: 20),
                
                // Career Page URL Field
                _buildInputField(
                  controller: _careerPageController,
                  label: 'Career Page URL',
                  hint: 'https://company.com/careers',
                  icon: Icons.work_outline_rounded,
                  validator: _validateUrl,
                  themeColors: themeColors,
                ),
                const SizedBox(height: 28),
                
                // Submit Button
                SizedBox(
                  width: double.infinity,
                  height: 54,
                  child: ElevatedButton(
                    onPressed: _isSubmitting ? null : _handleSubmit,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.transparent,
                      foregroundColor: Colors.white,
                      elevation: 0,
                      shadowColor: Colors.transparent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                      padding: EdgeInsets.zero,
                    ),
                    child: Ink(
                      decoration: BoxDecoration(
                        gradient: LinearGradient(
                          colors: _isSubmitting 
                              ? [
                                  themeColors[0].withOpacity(0.6),
                                  themeColors[1].withOpacity(0.6),
                                ]
                              : [themeColors[0], themeColors[1]],
                          begin: Alignment.centerLeft,
                          end: Alignment.centerRight,
                        ),
                        borderRadius: BorderRadius.circular(16),
                        boxShadow: _isSubmitting ? [] : [
                          BoxShadow(
                            color: themeColors[0].withOpacity(0.4),
                            blurRadius: 12,
                            offset: const Offset(0, 6),
                          ),
                        ],
                      ),
                      child: Container(
                        alignment: Alignment.center,
                        child: _isSubmitting
                            ? const SizedBox(
                                height: 22,
                                width: 22,
                                child: CircularProgressIndicator(
                                  strokeWidth: 2.5,
                                  valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                                ),
                              )
                            : Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Icon(
                                    Icons.add_circle_outline_rounded,
                                    size: 20,
                                  ),
                                  const SizedBox(width: 8),
                                  const Text(
                                    'Add Site',
                                    style: TextStyle(
                                      fontSize: 16,
                                      fontWeight: FontWeight.w600,
                                      letterSpacing: 0.2,
                                    ),
                                  ),
                                ],
                              ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildInputField({
    required TextEditingController controller,
    required String label,
    required String hint,
    required IconData icon,
    required String? Function(String?) validator,
    required List<Color> themeColors,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: TextStyle(
            fontSize: 15,
            fontWeight: FontWeight.w600,
            color: Color(0xFF2D3748), // Dark blue-grey
            letterSpacing: 0.2,
          ),
        ),
        const SizedBox(height: 10),
        TextFormField(
          controller: controller,
          validator: validator,
          style: TextStyle(
            color: Color(0xFF2D3748), // Dark blue-grey
            fontSize: 16,
            fontWeight: FontWeight.w500,
          ),
          decoration: InputDecoration(
            hintText: hint,
            hintStyle: TextStyle(
              color: Color(0xFF9CA3AF), // Light grey
              fontSize: 15,
              fontWeight: FontWeight.w400,
            ),
            prefixIcon: Container(
              margin: const EdgeInsets.all(12),
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    themeColors[0].withOpacity(0.15),
                    themeColors[1].withOpacity(0.1),
                  ],
                ),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Icon(
                icon,
                color: themeColors[0],
                size: 20,
              ),
            ),
            filled: true,
            fillColor: Color(0xFFFAFBFC), // Very light background
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(14),
              borderSide: BorderSide(
                color: Color(0xFFE2E8F0), // Light border
                width: 1.5,
              ),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(14),
              borderSide: BorderSide(
                color: Color(0xFFE2E8F0), // Light border
                width: 1.5,
              ),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(14),
              borderSide: BorderSide(
                color: themeColors[0],
                width: 2,
              ),
            ),
            errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(14),
              borderSide: BorderSide(
                color: Color(0xFFEF4444), // Red
                width: 1.5,
              ),
            ),
            focusedErrorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(14),
              borderSide: BorderSide(
                color: Color(0xFFEF4444), // Red
                width: 2,
              ),
            ),
            contentPadding: const EdgeInsets.symmetric(
              horizontal: 16,
              vertical: 18,
            ),
            errorStyle: TextStyle(
              color: Color(0xFFEF4444), // Red
              fontSize: 12,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
      ],
    );
  }
}
