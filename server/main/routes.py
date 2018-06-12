from werkzeug.http import HTTP_STATUS_CODES
from datetime import datetime
from flask import render_template, flash, redirect, url_for, request, jsonify
from flask_login import login_user, logout_user, current_user, login_required
from werkzeug.urls import url_parse
from main import app, db, models, forms
from main.forms import DeleteForm, LoginForm, UserRegistrationForm, AdminRegistrationForm, EditProfileForm, ResetPasswordRequestForm, ResetPasswordForm
from main.models import User
from main.email import send_password_reset_email
import json
import os


@app.before_first_request
def loadInitData():
    site_root = os.path.realpath(os.path.dirname(__file__))
    path = os.path.join(site_root, 'static', 'init.json')
    with open(path) as json_file:
        data = json.load(json_file)
        # for tag in ['products', 'categories', 'countries']:
        tag = 'products'
        if tag in data:
            for e in data[tag]:
                p = models.Product()
                p.from_dict(e)
                p.country_id = 0
                db.session.add(p)
        tag = 'categories'
        if tag in data:
            for e in data[tag]:
                c = models.Category()
                c.from_dict(e)

        tag = 'categories'
        if tag in data:
            for e in data[tag]:
                c = models.Category()
                c.from_dict(e)
                for p_id in e['productsList']:
                    p = models.Product.query.filter_by(barcode=p_id).first()
                    p.add_category(c)

        tag = 'countries'
        if tag in data:
            for e in data[tag]:
                c = models.Country_Of_Origin()
                c.from_dict(e)
                db.session.add(c)
                for p_id in e['productsList']:
                    p = models.Product.query.filter_by(barcode=p_id).first()
                    c.products.append(p)
            db.session.commit()


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


# Application clients endpoints
@app.route('/products/', methods=['GET'])
def products():
    products = models.Product.query.all()
    dict_products = [p.to_dict() for p in products]
    return jsonify(dict_products)


@app.route('/products/<barcode>/', methods=['GET'])
def get_product(barcode):
    product = models.Product.query.filter_by(barcode=barcode).first_or_404()
    return jsonify(product.to_dict())


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


# Admins endpoints
@app.route('/admin/products/delete/<barcode>/', methods=['DELETE'])
@login_required
def delete_product(barcode):
    product = models.Product.query.filter_by(barcode=barcode).first_or_404()
    db.session.delete(product)
    db.session.commit()
    return redirect(url_for('products'))


@app.route('/admin/products/delete/', methods=['GET', 'POST'])
@login_required
def delete_product_form():
    form = DeleteForm()
    if form.validate_on_submit():
        product = models.Product.query.filter_by(barcode=form.id.data).first_or_404()
        db.session.delete(product)
        db.session.commit()
        flash("Product was deleted")
        return redirect(url_for('delete_product_form'))
    return render_template('add.html', title='Delete product', form=form)


@app.route('/admin/products/add/', methods=['POST', 'GET'])
@login_required
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
            flash("Product was added")
            return redirect(url_for('add_product'))
        return render_template('add.html', title='Add product', form=form)

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


@app.route('/admin/products/map_to/category/', methods=['GET', 'POST'])
@login_required
def map_product_category():
    if not wants_json_response():
        form = forms.MapForm()
        if form.validate_on_submit():
            p = models.Product.query.filter_by(barcode=form.product_id.data).first_or_404()
            c = models.Category.query.get_or_404(form.object_id.data)
            p.add_category(c)
            db.session.add(c)
            db.session.commit()
            flash("Product category was added")
            redirect(url_for('map_product_category'))
        return render_template('add.html', title='Map product to category', form=form)


@app.route('/admin/products/map_to/country_of_origin/', methods=['GET', 'POST'])
@login_required
def map_product_country():
    if not wants_json_response():
        form = forms.MapForm()
        if form.validate_on_submit():
            p = models.Product.query.filter_by(barcode=form.product_id.data).first_or_404()
            c = models.Country_Of_Origin.query.get_or_404(form.object_id.data)
            c.products.append(p)
            db.session.add(c)
            db.session.commit()
            flash("Product country of origin was added")
            redirect(url_for('map_product_country'))
        return render_template('add.html', title='Map product to country of origin', form=form)


@app.route('/admin/products/<barcode>/add/category/<id>', methods=['GET', 'POST'])
@login_required
def add_product_category(barcode, id):
    p = models.Product.query.filter_by(barcode=barcode).first_or_404()
    c = models.Category.query.get_or_404(id)
    p.add_category(c)
    db.session.commit()
    return jsonify(p.to_dict())


@app.route('/admin/products/<barcode>/add/country/<id>', methods=['PUT'])
@login_required
def add_product_country(barcode, id):
    p = models.Product.query.filter_by(barcode=barcode).first_or_404()
    c = models.Country_Of_Origin.query.get_or_404(id)
    c.products.append(p)
    db.session.commit()
    return jsonify(p.to_dict())


@app.route('/admin/categories/add/', methods=['GET', 'POST'])
@login_required
def add_category():
    if not wants_json_response():
        form = forms.CategoryForm()
        if form.validate_on_submit():
            c = models.Category(name=form.name.data,
                                description=form.description.data)
            db.session.add(c)
            db.session.commit()
            flash("Category was added")
            redirect(url_for('add_category'))
        return render_template('add.html', title='Add category', form=form)

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


@app.route('/admin/categories/delete/<id>/', methods=['DELETE'])
@login_required
def delete_category(id):
    c = models.Category.query.filter_by(id=id).first_or_404()
    db.session.delete(c)
    db.session.commit()

    response = jsonify()
    response.status_code = 200
    response.headers['Location'] = url_for('get_categories')
    return response


@app.route('/admin/categories/delete/', methods=['GET', 'POST'])
@login_required
def delete_category_form():
    form = DeleteForm()
    if form.validate_on_submit():
        c = models.Category.query.filter_by(id=form.id.data).first_or_404()
        db.session.delete(c)
        db.session.commit()
        flash("Category was deleted")
        return redirect(url_for('delete_category_form'))
    return render_template('add.html', title='Delete category', form=form)


@app.route('/admin/countries_of_origin/add/', methods=['GET', 'POST'])
@login_required
def add_country_of_origin():
    if not wants_json_response():
        form = forms.CountryOfOriginForm()
        if form.validate_on_submit():
            c = models.Country_Of_Origin(country_name=form.name.data)
            db.session.add(c)
            db.session.commit()
            flash("Country of origin was added")
            redirect(url_for('add_country_of_origin'))
        return render_template('add.html', title='Add country of origin', form=form)

    data = request.get_json() or {}
    if "country_name" not in data:
        return bad_request("missing field")
    c = models.Country_Of_Origin()
    c.from_dict(data)
    db.session.add(c)
    db.session.commit()
    response = jsonify(c.to_dict())
    response.status_code = 201
    response.headers['Location'] = url_for('get_country_of_origin', id=c.id)
    return response


@app.route('/admin/countries_of_origin/delete/<id>/', methods=['DELETE'])
@login_required
def delete_country(id):
    c = models.Country_Of_Origin.query.filter_by(id=id).first_or_404()
    db.session.delete(c)
    db.session.commit()

    response = jsonify()
    response.status_code = 200
    response.headers['Location'] = url_for('get_countries_of_origin')
    return response


@app.route('/admin/countries_of_origin/delete/', methods=['GET', 'POST'])
@login_required
def delete_countries_of_origin_form():
    form = DeleteForm()
    if form.validate_on_submit():
        c = models.Country_Of_Origin.query.filter_by(id=form.id.data).first_or_404()
        db.session.delete(c)
        db.session.commit()
        flash("Country of origin was deleted")
        return redirect(url_for('delete_countries_of_origin_form'))
    return render_template('add.html', title='Delete Country of origin', form=form)


@app.before_request
def before_request():
    if current_user.is_authenticated:
        current_user.last_seen = datetime.utcnow()
        db.session.commit()


@app.route('/', methods=['GET', 'POST'])
@app.route('/index', methods=['GET', 'POST'])
@login_required
def index():
    return render_template('index.html', title='Home')


@app.route('/explore')
@login_required
def explore():
    links = []
    links.append(url_for('products'))
    links.append(url_for('get_product', barcode=1))
    links.append(url_for('get_categories'))
    links.append(url_for('get_category', id=1))
    links.append(url_for('get_category_products', id=1))
    links.append(url_for('get_countries_of_origin'))
    links.append(url_for('get_country_of_origin_products', id=1))
    return jsonify(links)


@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data).first()
        if user is None or not user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))
        login_user(user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')
        return redirect(next_page)
    return render_template('login.html', title='Sign In', form=form)


@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index'))


@app.route('/register', methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = UserRegistrationForm()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data)
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        flash('Congratulations, you are now a registered user!')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)


@app.route('/admin', methods=['GET'])
def show_admin_endpoints():
    return render_template('admin_urls.html')


@app.route('/admin/register', methods=['GET', 'POST'])
def register_admin():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = AdminRegistrationForm()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data)
        user.set_password(form.password.data)
        user.is_admin = True
        db.session.add(user)
        db.session.commit()
        flash('Congratulations, you are now a registered admin!')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)


@app.route('/reset_password_request', methods=['GET', 'POST'])
def reset_password_request():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = ResetPasswordRequestForm()
    if form.validate_on_submit():
        user = User.query.filter_by(email=form.email.data).first()
        if user:
            send_password_reset_email(user)
        flash('Check your email for the instructions to reset your password')
        return redirect(url_for('login'))
    return render_template('reset_password_request.html',
                           title='Reset Password', form=form)


@app.route('/reset_password/<token>', methods=['GET', 'POST'])
def reset_password(token):
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    user = User.verify_reset_password_token(token)
    if not user:
        return redirect(url_for('index'))
    form = ResetPasswordForm()
    if form.validate_on_submit():
        user.set_password(form.password.data)
        db.session.commit()
        flash('Your password has been reset.')
        return redirect(url_for('login'))
    return render_template('reset_password.html', form=form)


@app.route('/user/<username>')
@login_required
def user(username):
    user = User.query.filter_by(username=username).first_or_404()
    return render_template('user.html', user=user)


@app.route('/edit_profile', methods=['GET', 'POST'])
@login_required
def edit_profile():
    form = EditProfileForm(current_user.username)
    if form.validate_on_submit():
        current_user.username = form.username.data
        current_user.about_me = form.about_me.data
        db.session.commit()
        flash('Your changes have been saved.')
        return redirect(url_for('edit_profile'))
    elif request.method == 'GET':
        form.username.data = current_user.username
        form.about_me.data = current_user.about_me
    return render_template('edit_profile.html', title='Edit Profile',
                           form=form)


@app.route('/list/1', methods=['GET', 'POST'])
def add_basket():
    data = request.get_json() or {}
    print('kaka', data)
    if "barcode" not in data or "quantity" not in data:
        return bad_request("missing field")
    print('afafaf')
    i = models.ListItem()
    i.from_dict(data)
    db.session.add(i)
    db.session.commit()
    return redirect(url_for('get_basket'))


@app.route('/basket', methods=['GET'])
def get_basket():
    items = models.ListItem.query.all()
    dict_items = [i.to_dict() for i in items]
    return jsonify(dict_items)


@app.route('/basket/submit', methods=['GET'])
def submit_basket():
    items = models.ListItem.query.all()
    for i in items:
        db.session.delete(i)
    db.session.commit()
    response = jsonify()
    response.status_code = 201
    response.headers['Location'] = url_for('get_basket')
    return response


