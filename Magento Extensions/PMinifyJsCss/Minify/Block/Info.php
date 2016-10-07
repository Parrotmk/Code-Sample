<?php
/**
 * @category   PMinifyJsCss
 * @package    PMinifyJsCss_Minify
 * @author     PMinifyJsCss
 * @copyright  Copyright (c) 2015 PMinifyJsCss (http://www.PMinifyJsCss.com)
 * @license    http://opensource.org/licenses/osl-3.0.php  Open Software License (OSL 3.0)
 */
class PMinifyJsCss_Minify_Block_Info
    extends Mage_Adminhtml_Block_Abstract
    implements Varien_Data_Form_Element_Renderer_Interface
{

    /**
     * Render fieldset html
     *
     * @param Varien_Data_Form_Element_Abstract $element
     * @return string
     */
    public function render(Varien_Data_Form_Element_Abstract $element)
    {
		
        $logopath =	'http://www.PMinifyJsCss.com/media/PMinifyJsCss.gif';
        $html = <<<HTML
<div style="background:url('$logopath') no-repeat scroll 15px 15px #e7efef; border:1px solid #ccc; min-height:100px; margin:5px 0; padding:15px 15px 15px 140px;">
    <p>
        <strong>Magento Online Stores &amp; Extensions</strong><br />
        <a href="http://www.PMinifyJsCss.com" target="_blank">PMinifyJsCss</a> offers a wide choice of products and services for your online business.
    </p>
    <p>
        Website: <a href="http://www.PMinifyJsCss.com" target="_blank">www.PMinifyJsCss.com</a><br />
        Like, share and follow us on 
		<a href="https://www.facebook.com/PMinifyJsCss" target="_blank">Facebook</a>, 
		<a href="https://plus.google.com/+PMinifyJsCssCom" target="_blank">Google+</a>, 
		<a href="http://www.pinterest.com/PMinifyJsCss" target="_blank">Pinterest</a>, and 
        <a href="http://twitter.com/PMinifyJsCss" target="_blank">Twitter</a>.<br />
        If you have any questions send email at <a href="mailto:service@PMinifyJsCss.com">service@PMinifyJsCss.com</a>.
    </p>
</div>
<div>
	<p><strong>Products and services you might be interested in:</strong></p>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/image-optimizer-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/image-optimizer-for-magento.jpg" alt="Image Optimizer for Magento" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/minify-html-css-js-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/minify-html-css-js-for-magento.jpg" alt="Minify HTML CSS JS for Magento" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/professional-magento-installation" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/professional-magento-installation.jpg" alt="Professional Magento Installation" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/quick-search-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/quick-search-for-magento.jpg" alt="Quick Search for Magento" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/responsive-product-slider-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/responsive-product-slider-for-magento.jpg" alt="Responsive Product Slider for Magento" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/schema-org-microdata-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/schema-org-microdata-for-magento.jpg" alt="Schema.org Microdata for Magento" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/social-integrator-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/social-integrator-for-magento.jpg" alt="Social Integrator" style="border:1px solid #ccc;" />
	</a>
	<a href="http://www.PMinifyJsCss.com/products-and-services/magento/subcategories-grid-list-for-magento" target="_blank" style="margin: 0 15px 15px 0; display: inline-block;">
		<img src="http://www.PMinifyJsCss.com/media/PMinifyJsCss-promotional/subcategories-grid-list-for-magento.jpg" alt="Subcategories Grid/List" style="border:1px solid #ccc;" />
	</a>
</div>
HTML;
        return $html;
    }
}