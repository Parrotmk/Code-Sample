import 'package:flutter/material.dart';
import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';

class CategoryPage extends StatefulWidget
{
  @override
  CategoryPageState createState() => new CategoryPageState();
}

/**
 * DocumentID categoryId
 */
browseByCategory(categoryId)
{
  //DocumentReference documentReference = Firestore.instance.collection(path)
}


class CategoryPageState extends State<CategoryPage>
{
  //final CollectionReference collectionReference = Firestore.instance.collection('categories').snapshots();
  @override
  Widget build(BuildContext context) {
    return StreamBuilder(
        stream: Firestore.instance.collection("categories").snapshots(),
        builder: (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
          if(!snapshot.hasData)
          {
            return Container(
              child: Center( 
                 child: CircularProgressIndicator(),
              ),
            );
          }
          else{
             return Container(
               color: Colors.white,
               child: Scaffold(
                 appBar: AppBar(
                   title: Text('Browse by categories'),
                 ),
                 body: ListView(
                 children: 
                    getCategoriesItems(snapshot)
                ),
               ),
             );
          }
        });
  }

  

 List getCategoriesItems(AsyncSnapshot<QuerySnapshot> snapshot) {
     return snapshot.data.documents
        .map((docoument) => ListTile( 
          title: FlatButton(
              padding: EdgeInsets.all(10.0),
              color: Colors.white,
              child : Align( 
                alignment: Alignment(-1.0, 0.0),
                child: Text(docoument["name"], 
                style: TextStyle(fontSize: 18.0, fontWeight: FontWeight.w400, ),
                )
              ),
              onPressed: () { 
                browseByCategory(docoument.documentID);
                },
              )
            )
          ).toList();
  }
 
}