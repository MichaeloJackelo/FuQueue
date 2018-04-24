from flask import url_for, redirect, jsonify, render_template
from app import app, models, forms, db

@app.route('/')
@app.route('/index')
def index():
    return redirect(url_for('products'))


@app.route('/products/')
def products():
    products = models.Product.query.all()
    dict_products = [p.to_dict() for p in products]
    return jsonify(dict_products)


@app.route('/products/<id>/')
def product(id):
    product = models.Product.query.filter_by(id=id).first_or_404()
    return jsonify(product.to_dict())


@app.route('/products/add/', methods=['GET', 'POST'])
def add_product():
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

