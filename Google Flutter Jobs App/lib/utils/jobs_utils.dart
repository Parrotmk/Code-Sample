import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';


class JobsUtils
{
 
 Map _categories = new Map();
 Future<Map> getCategories() async
 {
  
    
   await Firestore.instance.collection('categories').snapshots().listen((onData) {
      List<DocumentSnapshot> categries =  onData.documents;
      categries.forEach(( DocumentSnapshot cat) {
        _categories[cat.documentID] = cat.data['name'];
      });
    });
    return _categories;
 }
}