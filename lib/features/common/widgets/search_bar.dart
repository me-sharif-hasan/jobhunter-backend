import 'dart:async';

import 'package:flutter/material.dart';

class ModernSearchBar extends StatefulWidget {
  final Function(String) onSearch;

  const ModernSearchBar({
    super.key,
    required this.onSearch,
  });

  @override
  State<ModernSearchBar> createState() => _ModernSearchBarState();
}

class _ModernSearchBarState extends State<ModernSearchBar> {
  final TextEditingController _searchController = TextEditingController();
  Timer? _debounce;
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50),
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Color(0xFFB2DFDB), // Light teal
            Color(0xFFFFF7E6), // Soft cream
          ],
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Row(
        children: [
          Expanded(
            child: TextField(
              style: const TextStyle(fontSize: 16, color: Colors.black87),
              controller: _searchController,
              decoration: const InputDecoration(
                hintText: 'Search here...',
                hintStyle: TextStyle(color: Colors.black54),
                border: InputBorder.none,
              ),
              onChanged: (e){
                //set timer to search within 500ms and cancel the previous timer
                if(_debounce?.isActive ?? false) _debounce!.cancel();
                _debounce = Timer(const Duration(milliseconds: 500), (){
                  widget.onSearch(_searchController.text);
                });
              },
            ),
          ),
          GestureDetector(
            onTap: () {
              widget.onSearch(_searchController.text);
            },
            child: Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Colors.teal,
                shape: BoxShape.circle,
                boxShadow: [
                  BoxShadow(
                    color: Colors.teal.withOpacity(0.5),
                    blurRadius: 8,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: const Icon(Icons.search, color: Colors.white),
            ),
          ),
        ],
      ),
    );
  }
}