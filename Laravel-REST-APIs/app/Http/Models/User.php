<?php

namespace App\Http\Models;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Database\Eloquent\Model;

class User extends Authenticatable
{
    use Notifiable;

	 /**
     * The table name in database.
     *
     * @var string
     */
    protected $table = "users";
	
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password',
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];
	
	public function getNameAttribute($value)
	{
		return ucfirst($value);
	}
	 
	public static function getUsers()
	{
		$users = self::all();
		return $users ;
	}
	public static function getUser($id)
	{
		$user = self::where('id',$id)->first();
		return $user ;
	}	
}
