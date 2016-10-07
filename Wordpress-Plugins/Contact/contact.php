<?php
/*
 * Plugin Name: Guest Book
 * Description:  .
 * Version: 3.0
 * Author: Manoj Kumar
 * License:  GPL2
 */
$siteurl = get_option('siteurl');
define('FOLDER_NAME_Banner', dirname(plugin_basename(__FILE__)));
define('URL_calltureproduct', $siteurl.'/wp-content/plugins/' . FOLDER_NAME_banner);
define('PATH_calltureproduct', dirname(__FILE__));
define('DIR_NAME', basename(PATH_banner));
//define( 'WP_CONTENT_DIR', ABSPATH . 'wp-content' );
// this is the table prefix
global $wpdb;
$table_prefix=$wpdb->prefix;
define('TABLE_PREFIX', $table_prefix);
function banner_install()
{
	

}

register_activation_hook(__FILE__,'banner_install');
register_deactivation_hook(__FILE__,'banner_uninstall');


function banner_uninstall()
{

}

add_action('admin_menu', 'adminMenu');
  
  function  adminMenu(){  
    add_menu_page(
		"Manage gallery",
		"Guests Book",
		'Administrator', 
		__FILE__,
		"admin_menu_list",
		plugins_url(). "/banner/images/menu.jpg"
	); 
	 add_submenu_page(__FILE__,'Guests','Guests Book','8','Guest','admin_view');
	add_submenu_page(null,'Guest Details','Guest Detail','8','view','admin_guest_view');
	add_submenu_page(__FILE__,'Contact Details','Contact List','8','contact','admin_contact');
	add_submenu_page(null,'Contact Details','Contact','8','contactview','admin_contact_view');

  }
  
  function admin_menu_list(){

 //echo "To show the record on page or post, use the below sort code</br><b> [wp_lightgallery].</b>";
// include 'admin_banner.php';
 include 'admin_view.php';
}


  
  function admin_banner(){
 }
   function admin_view(){
 include 'admin_view.php';
 }
 function admin_guest_view(){
	 include 'admin_guest_view.php';
 }
 function admin_contact(){
	 include 'admin_contact.php';
 }
 function admin_contact_view(){
	 include 'admin_contact_view.php';
 }
 
 add_shortcode("cul_product","prodImage");
 function prodImage($atts) 
{ 
     //  include 'admin_view.php';
	}
function do_output_buffer() {
        ob_start();
}
?>