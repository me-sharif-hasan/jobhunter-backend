
import 'package:intl/intl.dart';

String toLocalTime(DateTime time) {
  return DateFormat('hh:mm a, dd MMM, yyyy').format(time);
}