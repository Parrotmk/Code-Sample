# Laravel REST API

## How to install

You can do database connectivity in this file Run this command on console to add database tables : Laravel-REST-APIs/.env

After connecting application to database run this command to migrate tables:

`php artisan migrate:install` This will create a migrate repository in database and you can see the tables.

Run the command in console to start server: `php artisan serve`

Now you can call the APIs with URLs. for examples

1. http://localhost:8000/api/user - GET Method - GET ALL Users

2. http://localhost:8000/api/user/4 - GET Method - GET a particular user

3. http://localhost:8000/api/user/4  - DELETE Method - Delete a user

4. http://localhost:8000/api/user/4  - PUT Method - update a user

5. http://localhost:8000/api/user  - POST Method - Add a user


ROUTING CODE for GET

```php 
Route::group(array('prefix' => 'api'), function() {
	/* Get All users */
    Route::get('/user','APIController@show');
	
	/* Get a user by Id */
	Route::get('/user/{id}','APIController@get');
});

```
