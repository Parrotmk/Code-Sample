import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:geocoder/geocoder.dart';
import 'package:geolocator/geolocator.dart';
import 'package:shared_preferences/shared_preferences.dart';
  

class LocationUtils {
  /**
   * Shared preference object variable
   */
  SharedPreferences _oAuth;

  Geolocator geolocator = Geolocator();
  Position _position;
  Address _oLocation;
 
 


  /**
   * Get current latitude/Longitude position
   */
  Future<Position> findCurrentPosition() async {
    await geolocator.getCurrentPosition(LocationAccuracy.best).then((position) {
      _position = position;
    });
    
    return _position;
  }

/**
 * Get Default Address from cordinates
 */
  Future<Address> getCurrentLocation() async {
    await this.findCurrentPosition().then((Position position) async {
      print(position.latitude);
      print(position.longitude);
      _oLocation = await this
          .getAddressByPosition(position.latitude, position.longitude);
    });
    return _oLocation;
  }

/**
 * Get address by lat/long
 */
  Future<Address> getAddressByPosition(
      double latitude, double longitude) async {
    await Geocoder.local
        .findAddressesFromCoordinates(Coordinates(latitude, longitude))
        .then((address) {
      _oLocation = address.first;
    });
    return _oLocation;
  }

  /**
   * Get GeoPoint after comparing lat/long
   */

  Future<Map> getGeoPoint()  async{
    // ~1 mile  of lat and lon in degrees
    final lat = 0.0144927536231884;
    final lon = 0.0181818181818182;
    final distance = 150;
    double greaterLat;
    double greaterLon;
    Map points = Map<String, GeoPoint>();

    this.findCurrentPosition().then((position) {
      final lowerLat = position.latitude - (lat * distance);
      final lowerLon = position.longitude - (lon * distance);
      greaterLat = position.latitude + (lat * distance);
      greaterLon = position.longitude + (lon * distance);

      points['lesserGeopoint'] = GeoPoint(lowerLat, lowerLon);
      points['greaterGeopoint'] = GeoPoint(greaterLat, greaterLon);
      return points;
    });
  }
}
