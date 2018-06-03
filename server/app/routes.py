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
    return {}


@app.route('/products/', methods=['GET'])
def products():
    products = models.Product.query.all()
    dict_products = [p.to_dict() for p in products]
    return jsonify(dict_products)


@app.route('/products/<barcode>/', methods=['GET'])
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
    p.country_id = 0
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


@app.route('/products/<barcode>/add/category/<id>', methods=['POST'])
def add_product_category(barcode, id):
    p = models.Product.query.filter_by(barcode=barcode).first_or_404()
    c = models.Category.query.get_or_404(id)
    p.add_category(c)
    db.session.commit()
    return jsonify(p.to_dict())


@app.route('/products/<barcode>/add/country/<id>', methods=['POST'])
def add_product_country(barcode, id):
    p = models.Product.query.filter_by(barcode=barcode).first_or_404()
    c = models.Country_Of_Origin.query.get_or_404(id)
    c.products.append(p)
    db.session.commit()
    return jsonify(p.to_dict())


@app.route('/categories/', methods=['GET'])
def get_categories():
    categories = models.Category.query.all()
    list = [c.to_dict() for c in categories]
    return jsonify(list)


@app.route('/categories/<id>', methods=['GET'])
def get_category(id):
    category = models.Category.query.filter_by(id=id).first_or_404()
    return jsonify(category.to_dict())


@app.route('/categories/<id>/products/', methods=['GET'])
def get_category_products(id):
    category = models.Category.query.get_or_404(id)
    list = [p.to_dict() for p in category.products]
    return jsonify(list)


@app.route('/categories/add/', methods=['POST'])
def add_category():
    data = request.get_json() or {}
    if "name" not in data or "description" not in data:
        return bad_request("missing field")
    c = models.Category()
    c.from_dict(data)
    db.session.add(c)
    db.session.commit()
    response = jsonify(c.to_dict())
    response.status_code = 201
    response.headers['Location'] = url_for('get_categories', id=c.id)
    return response


@app.route('/countries_of_origin/', methods=['GET'])
def get_countries_of_origin():
    country_of_origin = models.Country_Of_Origin.query.all()
    list = [coo.to_dict() for coo in country_of_origin]
    return jsonify(list)


@app.route('/countries_of_origin/<id>', methods=['GET'])
def get_country_of_origin(id):
    country_of_origin = models.Country_Of_Origin.query.filter_by(id=id).first_or_404()
    return jsonify(country_of_origin.to_dict())


@app.route('/countries_of_origin/<id>/products/', methods=['GET'])
def get_country_of_origin_products(id):
    country_of_origin = models.Country_Of_Origin.query.get_or_404(id)
    list = [p.to_dict() for p in country_of_origin.products]
    return jsonify(list)


@app.route('/countries_of_origin/add/', methods=['POST'])
def add_country_of_origin():
    data = request.get_json() or {}
    if "country_name" not in data:
        return bad_request("missing field")
    c = models.Country_Of_Origin()
    c.from_dict(data)
    db.session.add(c)
    db.session.commit()
    response = jsonify(c.to_dict())
    response.status_code = 201
    response.headers['Location'] = url_for('get_countries_of_origin', id=c.id)
    return response