import 'dart:html';

void main() {
  querySelector("#sample_text_id")
      ..text = "Click me!"
      ..onClick.listen(reverseText);
}

void reverseText(MouseEvent event) {
  var text = querySelector("#sample_text_id").text;
  StringBuffer buffer = new StringBuffer();
  for (int i = text.length - 1; i >= 0; i--) {
    buffer.write(text[i]);
  }
  buffer.write("(" +  (text.length).toString() + ")");
  buffer.write("Hello World!");
  buffer.isNotEmpty;
  querySelector("#sample_text_id").text = buffer.toString();
}
