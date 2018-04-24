from app import db


class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128), index=True, unique=True)
    prize = db.Column(db.Float)
    file_name = db.Column(db.String(128))
    barcode = db.Column(db.Integer)
    description = db.Column(db.String(256))

    def to_dict(self):
        return {"id": self.id, "barcode": self.barcode, "name": self.name,
                "prize": self.prize, "file_name": self.file_name, "description": self.description}

    def __repr__(self):
        return '<Product {}>'.format(self.name)