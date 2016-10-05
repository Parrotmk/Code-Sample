# Laravel Web Application 
Laravel is a free, open-source PHP web framework, used for the development of web applications following the model–view–controller (MVC) architectural pattern.

# Routing 
on Routing file we check the auth of visiting user

```php
Route::get('/', function () {
    return view('welcome');
});

Auth::routes();
Route::get('/home', 'HomeController@index');
Route::get('/home/{id}', 'HomeController@destroy');

 ```
 
# MVC Parrern
In Our Application, We used MVC pattern in laravel means App/Http folder has following folders that have controllers, models and views.

## Controller  
This folder ontaines controller code with auth.To write Auth controller, laravel provides Artisan command 
` php artisan make:auth `

Controller code to get All users from database 

```php
 public function index()
    {
		/* Throught Model */
		$users = User::getUsers(); 
        return view('home',['users'=>$users]);
    }

 ```


## Model  
 To get All users from database we write the code into controller
 
 ```php
  public function index()
    {
		/* Throught Model */
		$users = User::getUsers(); 
        return view('home',['users'=>$users]);
    }
 ```
   
This controller call  the 'User' Model in application

 ```php  
  public static function getUsers()
	{
		$users = self::all();
		return $users ;
	}	
	
 ```
     
We also used some query building tools to write the mysql query

 ```php  
 DB::table('users')->where('id', '=', $id)->delete();     
 
 ```
 
## View
 
  ```php  
 @foreach ($users as $user)
 <tr>
        <td>{!! $user->name !!}</td>
        <td>{!! $user->email !!}</td>
        <td>{!! $user->created_at !!}</td>
		<td><a href="{{URL::to('/home/'. $user->id)}}">Delete</a></td>
      </tr>
    @endforeach

 ```
