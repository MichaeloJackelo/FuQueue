/*Responsive left sidebar*/
function w3_open() {
document.getElementById("mySidebar").style.display = "block";
}
function w3_close() {
document.getElementById("mySidebar").style.display = "none";
}
var arrowDirection='N';
var sortValue='name';
function orderBy(){
  if(arrowDirection=='N'){
    $('#sortArray').css({
    'transform': 'rotate(180deg)'
    });
    arrowDirection='S';
  }
  else{
    $('#sortArray').css({
    'transform': 'rotate(360deg)'
    });
    arrowDirection='N';
  }

  sortBy(sortValue);

}

//////////////////////LOGIC//////////////
/*Left sidebar pd*/
var productToAdd='';
/*Display records*/
var app = angular.module('app', []);
app.controller('headerController', function($scope){
     $scope.searchValue="";
     $scope.categories=[];
     $scope.providers=[];
     $scope.changeInputProvider=function(){
     };
     $scope.searchForProduct=function(){
        var productName=$("#searchInput").val();
        if(productName!=''){
            window.location.href="/secured/products?name="+productName;
        }
        else{
            window.location.href="/secured/products";
        }

     };
});
app.controller('resultController',function($scope, $filter) {
     $scope.orders=[];
     $scope.productList = [];
     $scope.selectedProduct={
        name: '',
        id: 0
     };
     $scope.selectProduct=function(name, id){
        $scope.selectedProduct.name=name;
        $scope.selectedProduct.id=id;
     };
     $scope.addProduct=function(productId, productName, category, productAmount,
                                    productMaxAmount, productPrice, productProvider, productImg){
        this.productList.push({
            id: productId,
            name: productName,
            category: category,
            amount: productAmount,
            maxAmount: productMaxAmount,
            price: productPrice,
            provider: productProvider,
            img: productImg
        });
     }
     $scope.removeProduct=function(){
        alert($scope.selectedProduct.name);
        alert($scope.selectedProduct.id);
        $.ajax({
            type : 'DELETE',
            url: '/secured/products/'+$scope.selectedProduct.id,
            contentType: "application/json; charset=utf-8",
            success : function() {
              alert("Succsses");
              window.location.reload();
            },
            error: function(){
              alert("Fail");
            }
        });
     }
     $scope.updateProductAjax=function(){
        alert("Update product");
        initProduct();
        $.ajax({
            type : 'PUT',
            url: '/secured/products/'+$scope.selectedProduct.id,
            data: JSON.stringify(product),
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            success : function(response) {
                alert("Succsses");
                 window.location.reload();
            },
            error: function(){
                alert("Something went wrong");
            }

        });
     }
     $scope.addToOrderAjax=function(){
        alert("Add to order Ajax");
        var orderName=$("#orderFilter").val();
     	$.ajax({
     		type : 'POST',
     		url: '/secured/orders/'+orderName+'/products/'+$scope.selectedProduct.id+'/amount/'+$("#orderAmount").val(),
     		contentType: "application/json; charset=utf-8",
     		dataType:'json',
     		success : function(response) {
     			alert("Succsses");
                 //window.location.reload();
     		},
     		error: function(){
     			alert("Something went wrong");
     		}

     	});
     }
     $scope.clearProducts=function(){
        this.productList=[];
     };
     $scope.sortProducts=function(sortParameter){
        if(arrowDirection=='N'){
           this.productList=$filter('orderBy')(this.productList, sortParameter);
        }
        else{
           this.productList=$filter('orderBy')(this.productList, sortParameter, true);
        }

     }
});

function initSearchValue(){
   	var scope = angular.element(document.getElementById("headerController")).scope();
   	scope.$apply(function() {
       var pathArray=(window.location.pathname+ window.location.search).split('=');
       if(pathArray[1]!=null){
            var nameValue=pathArray[1].split('&');
            nameValue[0]=nameValue[0].split('%20').join(' ');
            scope.searchValue=nameValue[0];
       }
    });


}
/*Get Products*/
function getProduct(id, name, category, amount, maxAmount, price, provider, img) {
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		scope.addProduct(id, name, category, amount, maxAmount, price, provider, img);
	});
}
function clearProductsList(){
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		scope.clearProducts();
	});
}
function getAllProductsAjax(){
	$.ajax({
		type : 'GET',
		dataType : 'JSON',
		success : function(product) {
			$.each(product, function(index, product) {
			    getProduct(product.id, product.name, product.category, product.amount,
			                product.maxAmount, product.price, product.provider, product.img);
			});
		},
		error : function() {
		}
	});
}



/*Add product*/
function initProduct(){
    productToAdd={"id": $('#barcode').val(), "name": $('#name').val(), "category": $('#category').val(), "provider": $('#provider').val(),
			    "price": $('#price').val(), "image":$('#image').val(), "amount": $('#amount').val(),
			        "maxAmount":$('#maxAmount').val(), "img":$('#image').val()};
}

function addProductAjax(){
	initProduct();
	$.ajax({
		type : 'POST',
		url: '/secured/products',
		data: JSON.stringify(productToAdd),
		contentType: "application/json; charset=utf-8",
		dataType:'json',
		success : function(response) {
			alert("Succsses");
            window.location.reload();
		},
		error: function(){
			alert("Something went wrong");
		}

	});
}
/*Header functions*/
function addCategory(category) {
	var scope = angular.element(document.getElementById("headerController")).scope();
	scope.$apply(function() {
	    if(category!=''){
	    	scope.categories.push(category);
	    }

	});
}
function addProvider(provider){
  var scope = angular.element(document.getElementById("headerController")).scope();
  scope.$apply(function() {
      if(category!=''){
        scope.providers.push(provider);
      }

  });
}
function addOrder(order){
  var scope = angular.element(document.getElementById("resultController")).scope();
  scope.$apply(function() {
      if(order!=''){
        scope.orders.push(order);
      }

  });
}
function getAllCategoriesAjax(){
	$.ajax({
		type : 'GET',
		dataType : 'JSON',
		url: '/secured/products/categories',
		success : function(category) {
            $.each(category, function(index, category) {
                addCategory(category);
			});
		},
		error : function() {
		    alert("Something went wrong with init categories");
		}
	});
}
function getAllProvidersAjax(){
  $.ajax({
    type : 'GET',
    dataType : 'JSON',
    url: '/secured/products/providers',
    success : function(provider) {
            $.each(provider, function(index, provider) {
                addProvider(provider);
      });
    },
    error : function() {
        alert("Something went wrong with init provider");
    }
  });
}
function getAllOrdersAjax(){
  $.ajax({
    type : 'GET',
    dataType : 'JSON',
    url: '/secured/orders/name',
    success : function(order) {
            $.each(order, function(index, order) {
                addOrder(order);
      });
    },
    error : function() {
        alert("Something went wrong with init provider");
    }
  });


}

/*Init website's components*/
function preparePage(){
    getAllProductsAjax();
    getAllCategoriesAjax();
    getAllProvidersAjax();
    getAllOrdersAjax();
    initSearchValue();
}


/*Menu service*/

function searchForProduct(){
  getProductsByNameAjax();
  var searchValue=$('#searchInput').val();
}

function filterResults(){
  var category=$("#categoryFilter").val();
  var provider=$("#providerFilter").val();
  var amount=$('#amountFilter').val();
  var searchValue=$('#searchInput').val();

  window.location.href=window.location.pathname+"?name="+searchValue+"&category="+category+"&provider="+provider;
}

function sortBy(sortParameter){
   sortValue=sortParameter;
   var scope = angular.element(document.getElementById("resultController")).scope();
      	scope.$apply(function() {
      		scope.sortProducts(sortParameter);
   });
}
