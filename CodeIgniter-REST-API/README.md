# CodeIgniter REST API 

You can find a full REST API implementation using Key Design Principles of REST.  

## Requirements

1. PHP 5.4 or greater
2. CodeIgniter 3.0+

## Installation Guidelines

You have to just downlload the CodeIgniter-REST-API folder from github and upload it on your server.

## Handling Requests

 
This allows you to implement a RESTful interface easily:
```php
class Users extends REST_Controller
{
  public function users_get($id)
  {
     // Display a single Users
  }
  public function users_post()
  {
    // Create a new user
  }
  
 public function users_list()
  {
    // Create all users
  }
  public function users_delete()
  {
    // delete a user
  }
  public function users_put($id)
  {
    // Update a user
  }

}
 ```

'REST_Controller' also supports `PUT` and `DELETE` methods, allowing you to support a truly RESTful interface.
HTTP Response can be in JSON format or in XML format.

Any responses you make from the class (see [responses](#responses) for more on this) will be serialised in the designated format.
