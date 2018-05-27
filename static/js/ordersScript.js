/*Show and hide order list*/
var isPresented=0;
var orderToAdd;
$(document).delegate('.mainColumn','click',function(){
	if($('#content').is(":visible") || isPresented==1){
		$('.products-table').hide();
		$('[colspan="8"]').hide();
		isPresented=0;
	}
	else{
		$('.products-table').show();
		$('[colspan="8"]').parent('tr').remove();
		$(this).parents('tr').after('<tr/>').next().append('<td colspan="8"/>').
			children('td').append('<div/>').
			children().css('background','#32383e').html($('#content').html());
		isPresented=1;
	}


});
var app = angular.module('orderApp', []);
app.controller('resultController', function($scope){
    $scope.currentOrder=0;
    $scope.orderSummary=0;
    $scope.currentProduct=0;
    $scope.currentRollNo=0;
    $scope.currentRollNoProduct=0;
    $scope.orders=[];

    $scope.setCurrentOrder=function(orderNumber){
        $scope.currentOrder=orderNumber-1;
        $scope.orderSummary=0;
        for(i=0;i<$scope.orders[$scope.currentOrder].products.length;i++){
                $scope.orderSummary+=$scope.orders[$scope.currentOrder].products[i].orderAmount*$scope.orders[$scope.currentOrder].products[i].price;
        }
    }
    $scope.setCurrentProduct=function(productNumber){
        $scope.currentProduct=productNumber-1;
    }
    $scope.addOrder=function(orderId, authorId, orderName, orderSendDate, orderReceiveDate, orderPredictedDate, orderStatusName){
        this.currentRollNo=this.currentRollNo+1;
        this.orders.push({
            id: orderId,
            rollNo:this.currentRollNo,
            author:authorId,
            name:orderName,
            sendDate:new Date(orderSendDate),
            receiveDate:new Date(orderReceiveDate),
            predictedDate:new Date(orderPredictedDate),
            status:orderStatusName,
            products:[]
        });
         return this.currentRollNo;
    }
    $scope.addProductToOrder=function(productId, productName, productCategory, productPrice, productProvider, productAmount, rollNo){
        productRollNo=$scope.orders[rollNo-1].products.length;
        $scope.orders[rollNo-1].products.push({
            rollNo:productRollNo+1,
            id: productId,
            name: productName,
            category:productCategory,
            price: productPrice,
            provider: productProvider,
            orderAmount: productAmount
         });

    }
    $scope.clearOrdersList=function(){
        this.orders=[];
        this.currentRollNo=0;
    }
    $scope.removeOrder=function(){
       $.ajax({
           type : 'DELETE',
           url: '/secured/orders/'+$scope.orders[$scope.currentOrder].id,
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
    $scope.removeProduct=function(){
        alert($scope.currentProduct);
       $.ajax({
           type : 'DELETE',
           url: '/secured/orders/'+$scope.orders[$scope.currentOrder].id+'/products/'+$scope.orders[$scope.currentOrder].products[$scope.currentProduct].id,
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
    $scope.changeStatusAjax=function(){
       $.ajax({
           type : 'PUT',
           url: '/secured/orders/'+$scope.orders[$scope.currentOrder].id+'/status/'+$scope.orders[$scope.currentOrder].status,
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

});

function setCurrentProduct(productNumber){
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		scope.setCurrentProduct(productNumber.getAttribute("data-productRollNo"));
	});
}
function initOrder(){
    orderToAdd={"name": $('#name').val(), "predictedDate": $('#predictedDate').val()};
}
function getOrder(orderId, authorId, name, sendDate, receiveDate, predictedDate, statusName){
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		rollNo=scope.addOrder(orderId, authorId, name, sendDate, receiveDate, predictedDate, statusName);
	});
	getAllProductsFromOrder(orderId, rollNo);
}
function addProductToOrder(id, name, category, price, provider, amount, rollNo){
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		scope.addProductToOrder(id, name, category, price, provider, amount, rollNo);
	});
}
function getAllProductsFromOrder(orderId, rollNo){
     $.ajax({
         type : 'GET',
         url: '/secured/orders/'+orderId+'/products',
         dataType : 'JSON',
         success : function(product) {
             $.each(product, function(index, product) {
                 addProductToOrder(product.id,product.name, product.category, product.price, product.provider, product.amount, rollNo);
             });
         },
         error : function() {
             alert("error");
         },
         complete : function(){
         }
      });
}
function clearOrdersList(){
	var scope = angular.element(document.getElementById("resultController")).scope();
	scope.$apply(function() {
		scope.clearOrdersList();
	});}
/*Ajax function*/
function getAllOrders(){
	$.ajax({
		type : 'GET',
		dataType : 'JSON',
		success : function(order) {
			$.each(order, function(index, order) {
			    getOrder(order.id, order.authorId, order.name, order.sendDate, order.receiveDate,
			                order.predictedDate, order.status.name);
			});
		},
		error : function() {
		}
	});
}
function getOrderByStatusAjax(statusId){
    clearOrdersList();
	$.ajax({
		type : 'GET',
		url: '/secured/orders/status/'+statusId,
		dataType : 'JSON',
		success : function(order) {
			$.each(order, function(index, order) {
			    getOrder(order.id, order.authorId, order.name, order.sendDate, order.receiveDate,
			                order.predictedDate, order.status.name);
			});
		},
		error : function() {
		}
	});
}

function getOrderByName(){
    //clearOrdersList();
    var orderName=$('#searchInput').val();
    window.location.href=window.location.href="/secured/orders?name="+orderName;
}
function getOrderByStatus(elm){
    getOrderByStatusAjax(elm.value);
}


function createOrderAjax(){
	initOrder();
	$.ajax({
		type : 'POST',
		url: '/secured/orders',
		data: JSON.stringify(orderToAdd),
		contentType: "application/json; charset=utf-8",
		dataType:'json',
		success : function(response) {
			alert("Succsses");
		},
		error: function(){
			alert("Something went wrong");
		}

	});
}