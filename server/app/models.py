from app import db

product_category_association_table = db.Table('product_category',
                                              db.Column("product_id", db.Integer, db.ForeignKey('product.id')),
                                              db.Column("category_id", db.Integer, db.ForeignKey('category.id')))


class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128))
    prize = db.Column(db.Float)
    file_name = db.Column(db.String(128))
    barcode = db.Column(db.Integer, unique=True)
    description = db.Column(db.String(256))
    categories = db.relationship("Category",
                                 secondary=product_category_association_table,
                                 backref=db.backref("products",
                                                    lazy='dynamic'),
                                 lazy='dynamic')

    def add_category(self, category):
        self.categories.append(category)

    def to_dict(self):
        return {"id": self.id, "barcode": self.barcode, "name": self.name,
                "prize": self.prize, "file_name": self.file_name, "description": self.description}

    def from_dict(self, data):
        for field in ['id', 'barcode', 'name', 'prize' , 'file_name', 'description']:
            if field in data:
                setattr(self, field, data[field])


class Category(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128))
    description = db.Column(db.String(256))

    def to_dict(self):
        return {"id": self.id, "name": self.name, "description": self.description}

    def from_dict(self, data):
        for field in ['id', 'name', 'description']:
            if field in data:
                setattr(self, field, data[field])


