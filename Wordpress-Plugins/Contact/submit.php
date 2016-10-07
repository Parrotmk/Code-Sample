<?php  
 include_once '../../../wp-load.php';
 global $wpdb;
 //echo "<pre>";print_r($wpdb);
 echo $wpdb->show_errors();
 $selid = $_POST['id'] ;
 $var = $_POST['value'];

if($var == 0){
	$wpdb->query( "UPDATE wp_guestbook  SET  guestbook_approve = 0 WHERE id='$selid'" );
	 $response = "Pending";
} // status should be 0
elseif($var == 1){
	$wpdb->query( "UPDATE wp_guestbook  SET  guestbook_approve = 1 WHERE id='$selid'" );
	 $response = "Approved";
} // status should be 1

else{
 $wpdb ->query("DELETE FROM wp_guestbook WHERE id=".$selid);
  $response = "Deleted";
} // data will be delete
echo $response;

?>

<select id="<?php echo $detail[0]->id;?>" onchange="getval(this);" class="select" name="select"  > 
</select>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script>
function getval(sel) {
	
	   var select = sel.value;
	   var id = sel.id;
	  
		
		jQuery.ajax({ 
		type:"post",
		url:"<?php echo plugins_url();?>/banner/submit.php",
		data: {'id': id , 'value': select },
		success:function(data){
			jQuery("#reponse").html(data);
			
		}
	});
    }
</script>