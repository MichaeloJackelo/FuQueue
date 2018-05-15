from flask import url_for, redirect, jsonify, request, render_template
from app import app, models, forms, db
from werkzeug.http import HTTP_STATUS_CODES


def error_response(status_code, message=None):
    payload = {'error': HTTP_STATUS_CODES.get(status_code, 'Unknown error')}
    if message:
        payload['message'] = message
    response = jsonify(payload)
    response.status_code = status_code
    return response

def wants_json_response():
    return request.accept_mimetypes['application/json'] >= \
        request.accept_mimetypes['text/html']

def bad_request(message):
    return error_response(400, message)

@app.route('/')
@app.route('/index')
def index():
    return redirect(url_for('products'))

@app.route('/products/')
def products():
    products = models.Product.query.all()
    dict_products = [p.to_dict() for p in products]
    return jsonify(dict_products)


@app.route('/products/<barcode>/')
def product(barcode):
    product = models.Product.query.filter_by(barcode=barcode).first_or_404()
    return jsonify(product.to_dict())


@app.route('/products/add/', methods=['POST'])
def add_product():
    if not wants_json_response():
        form = forms.ProductForm()
        if form.validate_on_submit():
            p = models.Product(name=form.name.data,
                               prize=form.prize.data,
                               file_name=form.file_name.data,
                               barcode=form.barcode.data,
                               description=form.description.data)
            db.session.add(p)
            db.session.commit()
            return redirect(url_for('products'))
        return render_template('add_product.html', title='Sign In', form=form)

    data = request.get_json() or {}
    if "name" not in data or "prize" not in data or "file_name" not in data or "barcode" \
        not in data or "description" not in data:
        return bad_request("missing field")
    p = models.Product()
    p.from_dict(data)
    db.session.add(p)
    db.session.commit()
    response = jsonify(p.to_dict())
    response.status_code = 201
    response.headers['Location'] = url_for('products', barcode=p.barcode)
    return response


@app.route('/products/add/', methods=['GET'])
def render_add_product():
    if not wants_json_response():
        form = forms.ProductForm()
        return render_template('add_product.html', title='Sign In', form=form)
    return bad_request("bad request")