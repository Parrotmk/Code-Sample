<?php
class PEnquiry_Enquiry_EnquiryController extends Mage_Core_Controller_Front_Action{
		public function IndexAction() {
				  
				  $this->loadLayout();   
				  $this->getLayout()->getBlock("head")->setTitle($this->__("Activate"));
						$breadcrumbs = $this->getLayout()->getBlock("breadcrumbs");
				  $breadcrumbs->addCrumb("home", array(
							"label" => $this->__("Home Page"),
							"title" => $this->__("Home Page"),
							"link"  => Mage::getBaseUrl()
					    ));

				  $breadcrumbs->addCrumb("enquiries", array(
							"label" => $this->__("Activate"),
							"title" => $this->__("Activate")
					   ));

				  $this->renderLayout(); 
				  }

		 public function enquiryAction() {
		          $postData = Mage::app()->getRequest()->getPost();	
				
				$name = $postData['name'];
				 $city = $postData['city'];
				 $code = $postData['postcode'];
				$country = $postData['country'];
				 $email = $postData['email'];
				 $product = $postData['product'];
				$psize = $postData['psize'];
				$store = $postData['store'];
				 $address = $postData['address'];
				 $terrain = '';
				 $cmd = '';
				 if(isset($postData['signup']))
				 {
					 foreach($postData['terrain'] as $terr){
						 $terrain .= $terr.', ';
					 }
					 $terrain = substr($terrain,0,-2);
					 $cmd =  ", terrain = '".$terrain."'";
					
				 }
				 
				  
				try{
				 /*  $model = Mage::getModel('enquiry/enquiry');
				  // $postData['telephone'] = preg_replace("/[^0-9]/","",$postData['telephone']);
				   $model->setData($postData);
				   $model->setCreatedDate(now());
				   $model->setUpdatedDate(now());
				   $model->setCreatedBy($postData['name']);
				   $model->save();
				   */
				   $cdate = now();
				   $udate = now();
				     $resource = Mage::getSingleton('core/resource');
				   $readConnection = $resource->getConnection('core_write');

				$query = "INSERT INTO enquiry SET name= '$name',
				 city= '$city',
				  postcode= '$postcode',
				   country= '$country',
				    email= '$email',
					 product= '$product',
					  psize= '$psize',
				  store= '$store',
					  address= '$address',
                     created_date = '$cdate',
					  updated_date = '$udate',
					  created_By = '$name'    $cmd 

					 ";
 
				$results = $readConnection->query($query);
				   

				 
    
				try {
					$customerEmailId = $email;
					$customerName = $name;
					
	
	
					$emailTemplateVariables = array();

					$emailTemplateVariables['name'] = $customerName;
					$emailTemplateVariables['city'] = $city;
					$emailTemplateVariables['postcode'] = $postcode;
					$emailTemplateVariables['country'] = $country;
					$emailTemplateVariables['email'] = $email;
					$emailTemplateVariables['product'] = $product;
					$emailTemplateVariables['psize'] = $psize;
					$emailTemplateVariables['store'] = $store;
					$emailTemplateVariables['address'] = $address;
                   
 
					$home = Mage::getBaseUrl();
					 $logo = $home.'media/email/logo/default/logo.png';
					
					//  mail('mkstallion89@gmail.com','helo-2','dfdf');
				
					$html = '<table width="600" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#000">
                 <tbody><tr>
    <td><table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tbody><tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td><a href="'.$home.'">
                                        <img width="107" height="69" src="'.$logo.'" alt="Inov-8" border="0">
                                    </a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td bgcolor="#efeeef"><table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
         <tbody><tr>
            <td height="10"></td>
          </tr>
                    <!-- Begin Content -->

          <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:25px;"></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px; padding-bottom: 15px;font-weight:bold;">Name </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$name.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">Address  </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px; padding-bottom: 15px;font-weight:bold;">'.$address .'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">City </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$city.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">Postcode </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$postcode.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px; padding-bottom: 15px;font-weight:bold;">Country </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$country.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">Email </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$email.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">Product you have purchased? </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$product.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px; padding-bottom: 15px;font-weight:bold;">Size of product? </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;padding-bottom: 15px; font-weight:bold;">'.$psize.'           </td>

          </tr>
		  
		   <tr>
            <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px;     padding-bottom: 15px; font-weight:bold;">Store you purchased the product? </td>
			 <td style="font-family:Arial, Helvetica, sans-serif; font-size:15px; padding-bottom: 15px;font-weight:bold;">'.$store.'           </td>

          </tr>
		  
		   
          <tr>
            <td>&nbsp;</td>
          </tr>
         
          </tbody></table></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      
    </tbody></table></td>
  </tr>
   <tr>
            <td>&nbsp;</td>
          </tr>
  
 
  <tr>
    <td>&nbsp;</td>
  </tr>






<tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td align="center" style="color:#fff; font-family:Arial, Helvetica, sans-serif; font-size:14px;">Thank you, Inov-8!</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</tbody></table>';

//echo $html; die;



$headers = "From: " . strip_tags($email) . "\r\n";
$headers .= "Reply-To: ". strip_tags($email) . "\r\n";
$headers .= "MIME-Version: 1.0\r\n";

$headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";

	$username=Mage::getStoreConfig('enquiries/settings/sendername');
	$receiveremail=Mage::getStoreConfig('enquiries/settings/senderemail');
					
					
mail($receiveremail, 'inov-8 | Activate Form',$html,$headers );















					
				} catch (Exception $e) {
					$errorMessage = $e->getMessage();
					return $errorMessage;
				}
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				   
				  // $message = $this->__('You Have Submitted Your Activate Successfully.');
                  // Mage::getSingleton('core/session')->addSuccess($message);
				   $this->_redirect("activate/thank-you");
				   }
				catch(Exception $ex)
				{
				
				}
		}
}