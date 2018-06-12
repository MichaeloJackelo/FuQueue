from datetime import datetime
from hashlib import md5
from time import time
from flask_login import UserMixin
from werkzeug.security import generate_password_hash, check_password_hash
import jwt
from main import app, db, login
from flask import url_for


product_category_association_table = db.Table('product_category',
                                              db.Column("product_id", db.Integer, db.ForeignKey('Product.barcode')),
                                              db.Column("category_id", db.Integer, db.ForeignKey('Category.id')))


class Product(db.Model):
    __tablename__ = 'Product'
    name = db.Column(db.String(128))
    prize = db.Column(db.Float)
    file_name = db.Column(db.String(128))
    barcode = db.Column(db.Integer, unique=True, primary_key=True)
    description = db.Column(db.String(256))
    country_id = db.Column(db.Integer, db.ForeignKey('Country_Of_Origin.id'))
    categories = db.relationship("Category",
                                 secondary=product_category_association_table,
                                 backref=db.backref("products",
                                                    lazy='dynamic'),
                                 lazy='dynamic')

    def add_category(self, category):
        self.categories.append(category)

    def to_dict(self):
        return {"barcode": self.barcode, "name": self.name,
                "prize": self.prize,
                "picture_url": url_for('static' , filename='products/' + self.file_name),
                "description": self.description}

    def from_dict(self, data):
        for field in ['barcode', 'name', 'prize', 'file_name', 'description']:
            if field in data:
                setattr(self, field, data[field])


class Category(db.Model):
    __tablename__ = 'Category'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128))
    description = db.Column(db.String(256))

    def to_dict(self):
        return {"id": self.id, "name": self.name, "description": self.description}

    def from_dict(self, data):
        for field in ['id', 'name', 'description']:
            if field in data:
                setattr(self, field, data[field])


class Country_Of_Origin(db.Model):
    __tablename__ = "Country_Of_Origin"
    id = db.Column(db.Integer, primary_key=True)
    country_name = db.Column(db.String(64))
    products = db.relationship("Product")

    def to_dict(self):
        return {"id": self.id, "country_name": self.country_name}

    def from_dict(self, data):
        for field in ['id', 'country_name']:
            if field in data:
                setattr(self, field, data[field])


class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128))
    about_me = db.Column(db.String(140))
    last_seen = db.Column(db.DateTime, default=datetime.utcnow)
    is_admin = db.Column(db.Boolean, default=False)

    def is_admin(self):
        if self.is_admin:
            return True
        raise 404

    def __repr__(self):
        return '<User {}>'.format(self.username)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def avatar(self, size):
        digest = md5(self.email.lower().encode('utf-8')).hexdigest()
        return 'https://www.gravatar.com/avatar/{}?d=identicon&s={}'.format(
            digest, size)

    def get_reset_password_token(self, expires_in=600):
        return jwt.encode(
            {'reset_password': self.id, 'exp': time() + expires_in},
            app.config['SECRET_KEY'], algorithm='HS256').decode('utf-8')

    @staticmethod
    def verify_reset_password_token(token):
        try:
            id = jwt.decode(token, app.config['SECRET_KEY'],
                            algorithms=['HS256'])['reset_password']
        except:
            return
        return User.query.get(id)


@login.user_loader
def load_user(id):
    return User.query.get(int(id))
